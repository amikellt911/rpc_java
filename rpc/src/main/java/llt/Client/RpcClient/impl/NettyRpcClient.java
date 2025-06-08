package llt.Client.RpcClient.impl;

import java.net.InetSocketAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import llt.Client.netty.nettyInitializer.NettyClientInitializer;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;
import llt.Client.RpcClient.RpcClient;
import llt.Client.serviceCenter.ServiceCenter;
import llt.Client.serviceCenter.ZKServiceCenter;

public class NettyRpcClient implements RpcClient{
    private ServiceCenter serviceCenter;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    static{
        bootstrap=new Bootstrap();
        eventLoopGroup=new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
    }

    public NettyRpcClient() throws InterruptedException{
        this.serviceCenter=new ZKServiceCenter();
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
            InetSocketAddress address=serviceCenter.serviceDiscovery(request.getInterfaceName());
            String host=address.getHostName();
            int port=address.getPort();
            try{
                ChannelFuture channelFuture=bootstrap.connect(host,port).sync();
                Channel channel=channelFuture.channel();
                //channel.writeAndFlush(request).sync();
                channel.writeAndFlush(request);
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key=AttributeKey.valueOf("rpcResponse");
                RpcResponse response=(RpcResponse) channel.attr(key).get();
                System.out.println("response:"+response);
                return response;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
    }
}

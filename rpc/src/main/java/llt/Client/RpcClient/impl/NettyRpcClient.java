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
import java.util.concurrent.CompletableFuture;

public class NettyRpcClient implements RpcClient{
    private ServiceCenter serviceCenter;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    static{
        bootstrap=new Bootstrap();
        eventLoopGroup=new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
    }

    public NettyRpcClient(ServiceCenter serviceCenter) throws InterruptedException{
        this.serviceCenter=serviceCenter;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
            InetSocketAddress address=serviceCenter.serviceDiscovery(request.getInterfaceName());
            String host=address.getHostName();
            int port=address.getPort();
            ChannelFuture channelFuture;
            try{
                channelFuture=bootstrap.connect(host,port).sync();
                Channel channel=channelFuture.channel();

                // 创建一个future来接收结果
                CompletableFuture<RpcResponse> future = new CompletableFuture<>();
                // 将future存入channel的AttributeMap，以便handler可以获取
                AttributeKey<CompletableFuture<RpcResponse>> key = AttributeKey.valueOf("RPC_FUTURE");
                channel.attr(key).set(future);

                channel.writeAndFlush(request);

                // 异步等待结果，而不是等待连接关闭
                return future.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
}

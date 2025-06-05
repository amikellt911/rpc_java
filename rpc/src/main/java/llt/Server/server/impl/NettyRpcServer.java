package llt.Server.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import llt.Server.netty.nettyInitializer.NettyServerInitializer;
import llt.Server.provider.ServiceProvider;
import llt.Server.server.RpcServer;
import lombok.AllArgsConstructor;
@AllArgsConstructor
public class NettyRpcServer implements RpcServer{
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup=new NioEventLoopGroup();
        NioEventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new NettyServerInitializer(serviceProvider));
            ChannelFuture channelFuture=serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    @Override
    public void stop() {
       
    }
}

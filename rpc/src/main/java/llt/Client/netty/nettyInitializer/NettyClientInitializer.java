package llt.Client.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import llt.Client.netty.handler.NettyClientHandler;
import llt.common.serializer.myCode.MyDecoder;
import llt.common.serializer.myCode.MyEncoder;
import llt.common.serializer.mySerializer.JsonSerializer;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline=ch.pipeline();
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}

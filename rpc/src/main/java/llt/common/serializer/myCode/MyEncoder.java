package llt.common.serializer.myCode;

import lombok.AllArgsConstructor;

import io.netty.handler.codec.MessageToByteEncoder;
import llt.common.Message.MessageType;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;
import llt.common.serializer.mySerializer.Serializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;

@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder<Object>{
    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println(msg.getClass());
        if(msg instanceof RpcRequest){
            out.writeShort(MessageType.REQUEST.getCode());
        }else if(msg instanceof RpcResponse){
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        out.writeShort(serializer.getType());
        byte[] bytes=serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}

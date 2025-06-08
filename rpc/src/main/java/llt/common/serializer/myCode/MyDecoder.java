package llt.common.serializer.myCode;

import lombok.AllArgsConstructor;

import io.netty.handler.codec.ByteToMessageDecoder;
import llt.common.Message.MessageType;
import llt.common.serializer.mySerializer.Serializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import java.util.List;

@AllArgsConstructor
public class MyDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        short messageType=in.readShort();
        if(messageType!=MessageType.REQUEST.getCode()&&messageType!=MessageType.RESPONSE.getCode()){
            throw new IllegalArgumentException("不支持的消息类型: " + messageType);
        }
        short serializerType=in.readShort();
        Serializer serializer=Serializer.getSerializerByCode(serializerType);
        if(serializer==null){
            throw new IllegalArgumentException("不支持的序列化器: " + serializerType);
        }
        int length=in.readInt();
        byte[] bytes=new byte[length];
        in.readBytes(bytes);
        Object obj=serializer.deserialize(bytes,messageType);
        out.add(obj);
    }
}

package llt.common.serializer.mySerializer;

public interface Serializer {
    byte[] serialize(Object object);
    //messageType:0-request,1-response,用于json时区分请求和响应
    Object deserialize(byte[] bytes,int messageType);
    //0-jdk,1-json
    int getType();
    //根据code获取序列化器
    static Serializer getSerializerByCode(int code){
        switch(code){
            case 0:
                return new JdkSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}

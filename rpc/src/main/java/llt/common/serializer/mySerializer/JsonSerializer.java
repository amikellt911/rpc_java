package llt.common.serializer.mySerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;

    public class JsonSerializer implements Serializer{

    @Override
    public byte[] serialize(Object object) {
        byte[] bytes=JSONObject.toJSONBytes(object);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj=null;
        switch(messageType){
            case 0:
                RpcRequest rpcRequest=JSON.parseObject(bytes,RpcRequest.class);
                Object[] args=new Object[rpcRequest.getParamTypes().length];
                for(int i=0;i<args.length;i++){
                    Class<?> paramsType=rpcRequest.getParamTypes()[i];
                    if(!paramsType.isAssignableFrom(rpcRequest.getParams()[i].getClass())){
                        args[i]=JSONObject.toJavaObject((JSONObject) rpcRequest.getParams()[i],rpcRequest.getParamTypes()[i]);
                    }else{
                        args[i]=rpcRequest.getParams()[i];
                    }
                }
                rpcRequest.setParams(args);
                obj=rpcRequest;
                break;
            case 1:
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                Class<?> dataType = response.getDataType();
                //判断转化后的response对象中的data的类型是否正确
                if(! dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(),dataType));
                }
                obj = response;
                break;
            default:
                throw new IllegalArgumentException("不支持的消息类型: " + messageType);
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}

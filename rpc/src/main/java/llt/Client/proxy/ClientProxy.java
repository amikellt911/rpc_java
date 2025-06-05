package llt.Client.proxy;

import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;
import llt.Client.RpcClient.RpcClient;
import llt.Client.RpcClient.impl.NettyRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientProxy implements InvocationHandler {
    private RpcClient rpcClient;

    public ClientProxy(){
        this.rpcClient=new NettyRpcClient();
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcResponse response = rpcClient.sendRequest(request);
        return response.getData();
    }

    public  <T>T getProxy(Class<T> clazz) {
        Object p=Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)p;
    }

}

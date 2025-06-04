package llt.Client.proxy;

import llt.Client.IOClient;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    private String host;
    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcResponse response = IOClient.sendRequest(host,port,request);
        return response.getData();
    }

    public  <T> T getProxy(Class<T> clazz) {
        Object p=Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)p;
    }

}

package llt.Client.proxy;

import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;
import llt.Client.RpcClient.RpcClient;
import llt.Client.RpcClient.impl.NettyRpcClient;
import llt.Client.serviceCenter.ServiceCenter;
import llt.Client.serviceCenter.ZKServiceCenter;
import llt.Client.retry.guavaRetry;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientProxy implements InvocationHandler {
    private RpcClient rpcClient;
    private ServiceCenter serviceCenter;
    public ClientProxy() throws InterruptedException{
        serviceCenter=new ZKServiceCenter();
        this.rpcClient=new NettyRpcClient(serviceCenter);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcResponse response;
        if(serviceCenter.checkRetry(request.getInterfaceName())){
            response=new guavaRetry().sendServiceRetry(request,rpcClient);
        }else{
            response=rpcClient.sendRequest(request);
        }
        return response.getData();
    }

    public  <T>T getProxy(Class<T> clazz) {
        Object p=Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)p;
    }

}

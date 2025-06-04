package llt.Server.server.work;

import lombok.AllArgsConstructor;
import llt.Server.provider.ServiceProvider;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

//实现runnable接口后可以放入线程池
@AllArgsConstructor
public class WorkThread implements Runnable{
    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        try { 
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest request = (RpcRequest) objectInputStream.readObject();
            RpcResponse response = getRespnse(request);
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException  e) {
            e.printStackTrace();
        }
    }

    private RpcResponse getRespnse(RpcRequest request) {
        String interfaceName = request.getInterfaceName();
        Object service = serviceProvider.getService(interfaceName);
        Method method = null;
        try{
            method = service.getClass().getMethod(request.getMethodName(),request.getParamTypes());
            Object invoke = method.invoke(service,request.getParams());
            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}

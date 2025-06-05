package llt.Client.RpcClient.impl;

import llt.Client.RpcClient.RpcClient;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleSocketRpcCilent implements RpcClient{


    private String host;
    private int port;

    public SimpleSocketRpcCilent(String host,int port){
        this.host=host;
        this.port=port;
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        //这种带括号的try-catch是java7新特性，可以自动关闭资源,防止资源泄露
        try(Socket socket=new Socket(host,port)){
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
            RpcResponse response=(RpcResponse) objectInputStream.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package llt.Server.serverRegister.impl;

import java.net.InetSocketAddress;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import llt.Server.serverRegister.ServiceRegister;

public class ZKServiceRegister implements ServiceRegister{
    private CuratorFramework client;
    private static final String ROOT_PATH="llt_rpc";
    private static final String RETRY_PATH = "retry";

    public ZKServiceRegister(){
        this.client=CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper连接成功");
    }

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress,boolean canRetry) {
        try {
            // serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            if(client.checkExists().forPath("/" + serviceName) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            // 路径地址，一个/代表一个节点
            String path = "/" + serviceName +"/"+ getServiceAddress(serverAddress);
            // 临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            if(canRetry){
               path="/"+RETRY_PATH+"/"+serviceName;
               if(client.checkExists().forPath(path) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
               }
            }
        } catch (Exception e) {
            System.out.println("此服务已存在");
        }
    }

    private String getServiceAddress(InetSocketAddress serverAddress){
        return serverAddress.getHostName()+":"+serverAddress.getPort();
    }
    private InetSocketAddress parseAddress(String address){
        String[] arr=address.split(":");
        String host=arr[0];
        int port=Integer.parseInt(arr[1]);
        return new InetSocketAddress(host,port);
    }
}

package llt.Client.serviceCenter;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import llt.Client.serviceCenter.balance.impl.ConsistencyHashBalance;
import llt.Client.cache.serviceCache;
import llt.Client.serviceCenter.ZkWatcher.watchZK;
import org.apache.curator.RetryPolicy;

public class ZKServiceCenter implements ServiceCenter {
    // zookeeper的客户端
    private CuratorFramework client;
    private serviceCache cache;
    private static final String ROOT_PATH = "llt_rpc";
    //幂等才可以重试，非幂等不可以重试（比如增删改都不可以）
    private static final String RETRY_PATH = "retry";
    private ConsistencyHashBalance loadBalancer;

    public ZKServiceCenter() throws InterruptedException{
        this.cache=new serviceCache();
        this.loadBalancer = new ConsistencyHashBalance();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(retryPolicy)
                .namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper连接成功");
        watchZK watchZK=new watchZK(client,cache);
        watchZK.watchToUpdate(ROOT_PATH);
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> serviceList = cache.getServiceFromCache(serviceName);
            if(serviceList==null)
            serviceList=client.getChildren().forPath("/" + serviceName);
            String str = loadBalancer.balance(serviceList);
            return parseAddress(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkRetry(String serviceName){
        boolean canRetry=false;
        try{
            List<String> serviceList=client.getChildren().forPath("/"+RETRY_PATH);
            for(String service:serviceList){
                if(service.equals(serviceName)){
                    System.out.println("服务"+serviceName+"在白名单上，可进行重试");
                    canRetry=true;
                    break;
                }
            }
            return canRetry;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    private InetSocketAddress parseAddress(String str) {
        String[] arr = str.split(":");
        String host = arr[0];
        int port = Integer.parseInt(arr[1]);
        return new InetSocketAddress(host, port);
    }

    // 地址 -> XXX.XXX.XXX.XXX:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress) {
            return serverAddress.getHostName() +
                    ":" +
                    serverAddress.getPort();
        }
    }
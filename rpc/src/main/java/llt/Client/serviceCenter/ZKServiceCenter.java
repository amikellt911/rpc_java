package llt.Client.serviceCenter;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.RetryPolicy;

public class ZKServiceCenter implements ServiceCenter {
    // zookeeper的客户端
    private CuratorFramework client;

    private static final String ROOT_PATH = "llt_rpc";

    public ZKServiceCenter() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(retryPolicy)
                .namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper连接成功");
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> serviceList = client.getChildren().forPath("/" + serviceName);
            String str = serviceList.get(0);
            return parseAddress(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
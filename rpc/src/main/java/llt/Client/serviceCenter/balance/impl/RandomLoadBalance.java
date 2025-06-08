package llt.Client.serviceCenter.balance.impl;

import java.util.List;
import java.util.Random;

import llt.Client.serviceCenter.balance.LoadBalance;

public class RandomLoadBalance implements LoadBalance{
    @Override
    public String balance(List<String> serviceAddresses) {
        Random random=new Random();
        int choose=random.nextInt(serviceAddresses.size());
        System.out.println("负载均衡选择了"+choose+"服务器");
        return serviceAddresses.get(choose);
    }
    @Override
    public void addNode(String node) {
        
    }
    @Override
    public void deleteNode(String node) {
        
    }
}

package llt.Client.serviceCenter.balance.impl;

import java.util.List;

import llt.Client.serviceCenter.balance.LoadBalance;

public class RoundLoadBalance implements LoadBalance{
    private int choose=-1;
    @Override
    public String balance(List<String> serviceAddresses) {
        choose++;
        choose=choose%serviceAddresses.size();
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

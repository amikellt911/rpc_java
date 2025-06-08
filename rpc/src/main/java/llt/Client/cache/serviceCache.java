package llt.Client.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
public class serviceCache {
    private static Map<String,List<String>> cache=new HashMap<>();

    public void addServiceToCache(String serviceName,String serviceAddress){
        if(cache.containsKey(serviceName)){
            cache.get(serviceName).add(serviceAddress);
            System.out.println("服务"+serviceName+"已存在，添加服务地址"+serviceAddress);
        }else{
            List<String> serviceList=new ArrayList<>();
            serviceList.add(serviceAddress);
            cache.put(serviceName,serviceList);
            System.out.println("服务"+serviceName+"不存在，添加服务地址"+serviceAddress);
        }
    }
    public void replaceServiceAddress(String serviceName,String oldServiceAddress,String newServiceAddress){
        if(cache.containsKey(serviceName)){
            cache.get(serviceName).remove(oldServiceAddress);
            cache.get(serviceName).add(newServiceAddress);
            System.out.println("服务"+serviceName+"已存在，替换服务地址"+oldServiceAddress+"为"+newServiceAddress);
        }else{
            System.out.println("服务"+serviceName+"不存在，无法替换服务地址");
        }
    }
    public List<String> getServiceFromCache(String serviceName){
        if(cache.containsKey(serviceName)){
            return cache.get(serviceName);
        }else{
            return null;
        }
    }
    public void removeServiceAddressFromCache(String serviceName,String address){
        if(cache.containsKey(serviceName)){
            cache.get(serviceName).remove(address);
            System.out.println("服务"+serviceName+"地址："+address+"已从缓存中移除");
        }else{
            System.out.println("服务"+serviceName+"地址："+address+"不存在，无法移除");
        }
    }
}

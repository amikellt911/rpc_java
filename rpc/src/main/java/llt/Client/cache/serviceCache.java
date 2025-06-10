package llt.Client.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class serviceCache {
    private static final Map<String, List<String>> serviceMap = new ConcurrentHashMap<>();

    public static void addServiceToCache(String serviceName,String serviceAddress){
        if(serviceMap.containsKey(serviceName)){
            List<String> addressList=serviceMap.get(serviceName);
            addressList.add(serviceAddress);
        }else {
            List<String> addressList=new ArrayList<>();
            addressList.add(serviceAddress);
            serviceMap.put(serviceName,addressList);
        }
    }
    public static void replaceServiceAddress(String serviceName,String oldServiceAddress,String newServiceAddress){
        if(serviceMap.containsKey(serviceName)){
            List<String> addressList=serviceMap.get(serviceName);
            addressList.remove(oldServiceAddress);
            addressList.add(newServiceAddress);
        }
    }

    public static List<String> getServiceAddressList(String serviceName){
        return serviceMap.get(serviceName);
    }
    public static void removeServiceAddress(String serviceName,String address){
        if(serviceMap.containsKey(serviceName)){
            List<String> addressList=serviceMap.get(serviceName);
            addressList.remove(address);
        }
    }
}

package llt.Client.serviceCenter.balance.impl;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.UUID;

import llt.Client.serviceCenter.balance.LoadBalance;

public class ConsistencyHashBalance implements LoadBalance{

    private final TreeMap<Integer,String> virtualNodes=new TreeMap<Integer,String>();
    private static final int VIRTUAL_NODES=5;
    private List<String> nodes=new LinkedList<String>();
    private String[] servers=null;

    private void init(List<String> serviceAddresses) {
        for(String server:serviceAddresses) {
            nodes.add(server);
            for(int i=0;i<VIRTUAL_NODES;i++) {
                String virtualNodeName=server+"&&VN"+i;
                int hash=getHash(virtualNodeName);
                virtualNodes.put(hash,virtualNodeName);
            }   
        }
    }

    public int getHash(String key) {
        final long H = 0x6a09e667f3bcc908L;
        final long L = 0xbb67ae8584caa73bL;
        final long P = 1L << 32;
        long hash = H;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash << 31) + key.charAt(i);
            hash = hash ^ (hash >> 33);
        }
        hash = hash * L;
        hash = hash ^ (hash >> 33);
        hash = hash * P;
        hash = hash ^ (hash >> 33);
        return (int) (hash & 0xffffffffL);
    }

    private String getServer(String node) {
        if (virtualNodes.isEmpty()) {
            return null;
        }
        int hash=getHash(node);
        SortedMap<Integer,String> sortedMap=virtualNodes.tailMap(hash);
        Integer key=null;
        if(sortedMap.isEmpty()) {
            key=virtualNodes.firstKey();
        }else{
            key=sortedMap.firstKey();
        }
        String virtualNodeName=virtualNodes.get(key);
        return virtualNodeName.substring(0,virtualNodeName.indexOf("&&"));
    }

    @Override
    public String balance(List<String> serviceAddresses) {
        List<String> currentNodes = new LinkedList<>(this.nodes);

        for (String node : currentNodes) {
            if (!serviceAddresses.contains(node)) {
                deleteNode(node);
            }
        }

        for (String serviceAddress : serviceAddresses) {
            addNode(serviceAddress);
        }

        if (virtualNodes.isEmpty()) {
            return null;
        }

        String randomNode=UUID.randomUUID().toString();
        return getServer(randomNode);
    }
    @Override
    public void addNode(String node) {
        if(!nodes.contains(node)) {
            nodes.add(node);
            for(int i=0;i<VIRTUAL_NODES;i++) {
                String virtualNodeName=node+"&&VN"+i;
                int hash=getHash(virtualNodeName);
                virtualNodes.put(hash,virtualNodeName);
            }
        }
    }
    @Override
    public void deleteNode(String node) {
        if(nodes.contains(node)) {
            nodes.remove(node);
            for(int i=0;i<VIRTUAL_NODES;i++) {
                String virtualNodeName=node+"&&VN"+i;
                int hash=getHash(virtualNodeName);
                virtualNodes.remove(hash);
            }
        }
    }
}

package llt.Client.serviceCenter.balance;

import java.util.List;

public interface LoadBalance {
    String balance(List<String> serviceAddresses);
    void addNode(String node);
    void deleteNode(String node);
}

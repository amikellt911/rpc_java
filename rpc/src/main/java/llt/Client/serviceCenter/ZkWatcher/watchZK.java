package llt.Client.serviceCenter.ZkWatcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.ChildData;
import llt.Client.cache.serviceCache;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class watchZK {
    private CuratorFramework client;

    serviceCache cache;

    public void watchToUpdate(String path) throws InterruptedException{
        CuratorCache curatorCache=CuratorCache.build(client,"/");
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData oldData, ChildData newData) {
                switch(type.name()){
                    case "NODE_CREATED":
                        String[] pathList=parsePath(newData);
                        if(pathList.length<=2) break;
                        else {
                            String serviceName=pathList[1];
                            String address=pathList[2];
                            cache.addServiceToCache(serviceName, address);
                        }
                        break;
                    case "NODE_DELETED":
                        String[] pathList_d=parsePath(oldData);
                        if(pathList_d.length<=2) break;
                        else{
                            String serviceName=pathList_d[1];
                            String address=pathList_d[2];
                            cache.removeServiceAddress(serviceName, address);
                        }
                        break;
                    case "NODE_CHANGED":
                        String[] oldPathList=parsePath(oldData);
                        String[] newPathList=parsePath(newData);
                        cache.replaceServiceAddress(oldPathList[1],oldPathList[2],newPathList[2]);
                        break;
                    default:
                        break;
                }
            }
        });
        //启动监听
        curatorCache.start();
    }
    private String[] parsePath(ChildData data){
        String path=data.getPath();
        String[] pathList=path.split("/");
        return pathList;
    }
}

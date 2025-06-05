package llt.Server.provider;

import java.net.InetSocketAddress;
import llt.Server.serverRegister.ServiceRegister;
import llt.Server.serverRegister.impl.ZKServiceRegister;
import java.util.HashMap;
import java.util.Map;

//存放服务
public class ServiceProvider {
    private Map<String,Object> interfaceProvider;
    private ServiceRegister serviceRegister;
    private String host;
    private int port;
    public ServiceProvider(String host,int port) {
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
        this.host=host;
        this.port=port;
    }

    public void provideServiceInterface(Object service){
        
        //一个服务可能通过多个接口实现，所以要是数组
        //key是接口名（也是提供给客户端的类名，value是服务实现类）
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for(Class<?> i:interfaces){
            interfaceProvider.put(i.getName(),service);
            serviceRegister.register(i.getName(),new InetSocketAddress(host,port));
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}

package llt.Client;

import llt.Client.proxy.ClientProxy;
import llt.common.service.UserService;
import llt.common.pojo.User;

public class TestClient {
    
    public static void main(String[] args) throws InterruptedException{
        ClientProxy clientProxy=new ClientProxy();
        UserService proxy=clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1175);
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(1175).name("llt").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}

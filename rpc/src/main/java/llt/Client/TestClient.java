package llt.Client;

import llt.Client.proxy.ClientProxy;
import llt.common.pojo.Blog;
import llt.common.service.BlogService;
import llt.common.service.UserService;
import llt.common.pojo.User;

public class TestClient {
    
    public static void main(String[] args) throws InterruptedException{
        ClientProxy clientProxy=new ClientProxy();

        // 1. 调用UserService
        UserService userServiceProxy =clientProxy.getProxy(UserService.class);
        User user = userServiceProxy.getUserByUserId(1175);
        System.out.println("从服务端得到的user="+user);

        User u=User.builder().id(1175).name("llt").sex(true).build();
        Integer id = userServiceProxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);

        System.out.println("==============================================");

        // 2. 调用BlogService
        BlogService blogServiceProxy = clientProxy.getProxy(BlogService.class);
        Blog blog = blogServiceProxy.getBlogById(666);
        System.out.println("从服务端得到的blog="+blog);
    }
}

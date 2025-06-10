package llt.Server;

import llt.Server.server.RpcServer;
import llt.common.service.impl.UserServiceImpl;
import llt.common.service.UserService;
import llt.Server.server.impl.NettyRpcServer;
import llt.Server.provider.ServiceProvider;
import llt.common.service.BlogService;
import llt.common.service.impl.BlogServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("127.0.0.1",9999);
        serviceProvider.provideServiceInterface(userService,true);
        serviceProvider.provideServiceInterface(blogService, false);

        RpcServer rpcServer=new NettyRpcServer(serviceProvider);
        rpcServer.start(9999);
    }
}

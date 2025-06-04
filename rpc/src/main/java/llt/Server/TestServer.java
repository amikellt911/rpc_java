package llt.Server;

import llt.Server.server.RpcServer;
import llt.common.service.impl.UserServiceImpl;
import llt.common.service.UserService;
import llt.Server.server.impl.SimpleRpcServer;
import llt.Server.provider.ServiceProvider;
public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new SimpleRpcServer(serviceProvider);
        rpcServer.start(9999);
    }
}

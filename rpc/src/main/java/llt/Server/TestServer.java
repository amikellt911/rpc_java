package llt.Server;

import llt.Server.server.RpcServer;
import llt.common.service.impl.UserServiceImpl;
import llt.common.service.UserService;
import llt.Server.server.impl.NettyRpcServer;
import llt.Server.provider.ServiceProvider;
public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("127.0.0.1",9999);
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new NettyRpcServer(serviceProvider);
        rpcServer.start(9999);
    }
}

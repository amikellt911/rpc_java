package llt.Server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import llt.Server.provider.ServiceProvider;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@AllArgsConstructor
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>{
    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response= getResponse(request);
        ctx.writeAndFlush(response);
        ctx.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    private RpcResponse getResponse(RpcRequest request){
        String interfaceName=request.getInterfaceName();
        Object service=serviceProvider.getService(interfaceName);
        Method method=null;
        try {
            method= service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            Object invoke=method.invoke(service,request.getParams());
            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}

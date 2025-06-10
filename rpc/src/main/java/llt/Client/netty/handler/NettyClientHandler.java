package llt.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import llt.common.Message.RpcResponse;

import java.util.concurrent.CompletableFuture;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        AttributeKey<CompletableFuture<RpcResponse>> key = AttributeKey.valueOf("RPC_FUTURE");
        CompletableFuture<RpcResponse> future = ctx.channel().attr(key).get();
        if (future != null) {
            future.complete(msg);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 同样，如果发生异常，也应该通知CompletableFuture
        AttributeKey<CompletableFuture<RpcResponse>> key = AttributeKey.valueOf("RPC_FUTURE");
        CompletableFuture<RpcResponse> future = ctx.channel().attr(key).get();
        if (future != null) {
            future.completeExceptionally(cause);
        }
        cause.printStackTrace();
        ctx.close();
    }
}


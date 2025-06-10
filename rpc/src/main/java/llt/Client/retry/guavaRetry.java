package llt.Client.retry;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Attempt;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import llt.Client.RpcClient.RpcClient;
import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;

public class guavaRetry {
    private RpcClient rpcClient;
    public RpcResponse sendServiceRetry(RpcRequest rpcRequest,RpcClient rpcClient){
        this.rpcClient=rpcClient;
        Retryer<RpcResponse> retryer=RetryerBuilder.<RpcResponse>newBuilder()
        .retryIfException()
        .retryIfResult(response->Objects.equals(response.getCode(),500))
        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
        .withWaitStrategy(WaitStrategies.fixedWait(1000,TimeUnit.MILLISECONDS))
        .withRetryListener(new RetryListener() {
            @Override
            public <V> void onRetry(Attempt<V> attempt) {
                // System.out.println("重试次数"+attempt.getAttemptNumber());
            }
        })
        .build();
        try{
            return retryer.call(()->rpcClient.sendRequest(rpcRequest));
        }catch(Exception e){
            e.printStackTrace();
        }
        return RpcResponse.fail();
    }
}

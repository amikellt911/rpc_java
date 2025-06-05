package llt.Client.RpcClient;

import llt.common.Message.RpcRequest;
import llt.common.Message.RpcResponse;

public interface RpcClient {
    public RpcResponse sendRequest(RpcRequest request);
}

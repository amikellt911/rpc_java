package llt.common.Message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcResponse implements Serializable {
    //状态码
    private int code;
    private String message;
    //具体数据
    private Object data;
    //构造成功信息
    public static RpcResponse success(Object data){
        return RpcResponse.builder()
                .code(200)
                .message("success")
                .data(data)
                .build();
    }
    public static RpcResponse fail(){
        return RpcResponse.builder()
                .code(500)
                .message("fail")
                .build();
    }
}

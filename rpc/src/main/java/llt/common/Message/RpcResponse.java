package llt.common.Message;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    //状态码
    private int code;
    private String message;
    //具体数据
    private Object data;
    //数据类型
    private Class<?> dataType;
    //构造成功信息
    public static RpcResponse success(Object data){
        return RpcResponse.builder()
                .code(200)
                .message("success")
                .data(data)
                .dataType(data != null ? data.getClass() : null)
                .build();
    }
    public static RpcResponse fail(){
        return RpcResponse.builder()
                .code(500)
                .message("fail")
                .build();
    }
}

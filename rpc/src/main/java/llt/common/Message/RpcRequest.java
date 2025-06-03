package llt.common.Message;

import lombok.Data;
import lombok.Builder;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable{
    //接口名
    private String interfaceName;
    //方法名
    private String methodName;
    //参数列表
    private Object[] params;
    //参数类型
    private Class<?>[] paramTypes;
}

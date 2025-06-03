package llt.common.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;//全参
import lombok.Builder;//链式调用构造函数
import lombok.NoArgsConstructor;//无参
import lombok.Data;//getter,setter,toString,equals,hashCode

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private Integer id;
    private String name;
    private Boolean sex;
}

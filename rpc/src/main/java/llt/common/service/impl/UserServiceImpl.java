package llt.common.service.impl;

import llt.common.pojo.User;
import llt.common.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了"+id+"的用户");
        return User.builder().id(id).name(UUID.randomUUID().toString()).sex(new Random().nextBoolean()).build();
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("插入数据成功："+user.getName());
        return user.getId();
    }
}

package llt.common.service;
import llt.common.pojo.User;
public interface UserService {
    User getUserByUserId(Integer id);
    Integer insertUserId(User user);
}

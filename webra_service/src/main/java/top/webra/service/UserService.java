package top.webra.service;

import top.webra.bean.ResponseBean;
import top.webra.pojo.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:08
 * @Description: --
 */
public interface UserService {

    /**
     * 获取用户列表  根据检索内容
     * @param departmentId      部门id
     * @param username          用户名
     * @param phone             手机号
     * @param state             状态0/1
     * @param createDateStart   创建起始范围
     * @param createDateEnd     创建结束范围
     * @param page              页码
     */
    ResponseBean getUserList(String token,
                             Integer departmentId,
                             String username,
                             Integer phone,
                             Integer state,
                             String createDateStart,
                             String createDateEnd,
                             Integer page);

    /**
     * 根据id获取用户信息
     * @param userId 用户id
     */
    ResponseBean selectUser(Integer userId);

    /**
     * 模糊查询nickname
     */
    ResponseBean selectUserByNickname();

    /**
     * 新增一个用户
     * @param user 用户对象
     */
    ResponseBean saveUser(String token, User user);

    /**
     * 根据id 修改用户状态
     * @param id 用户id
     */
    ResponseBean updateUserState(String token, Integer id);

    /**
     * 更新用户密码
     * @param id            用户id
     * @param rootPassword  root密码
     * @param newPassword   用户新密码
     */
    ResponseBean updatePassword(String token, Integer id, String  rootPassword, String newPassword);


    /**
     * 删除一个用户
     * @param id 用户id
     */
    ResponseBean deleteUser(String token, Integer id);

    /**
     * 批量删除用户
     * @param ids 用户ids
     */
    ResponseBean deleteUsers(String token, String ids);
}

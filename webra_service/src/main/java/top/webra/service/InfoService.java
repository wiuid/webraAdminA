package top.webra.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/3/22 12:07
 * @Description: --
 */
public interface InfoService {
    /**
     * 根据token获取个人头像地址
     * @param token     解析
     * @return 头像地址
     */
    String getAvatar(String token);

    /**
     * 从token中获取用户id，根据用户id返回个人信息
     * @param token 解析
     * @return 个人信息
     */
    String getInfo(String token);

    /**
     * 根据id 修改用户个人信息，token用于验证是否是个人
     * @param token         解析
     * @param id            用户id
     * @param nickname      昵称
     * @param phone         手机
     * @param email         邮箱
     * @return yes/no
     */
    String setInfo(String token,
                         Integer id,
                         String nickname,
                         String phone,
                         String email);

    /**
     * token用于验证是否修改的是个人密码
     * old用于未更改之前的密码是否匹配
     * @param token         解析
     * @param id            用户id
     * @param oldPassword   旧密码
     * @param newPassword   新密码
     * @return yes/no
     */
    String setPassword(String token,
                             Integer id,
                             String oldPassword,
                             String newPassword);

    /**
     * 修改头像
     * @param token 安全判定
     * @param id    用户id
     * @param base64    图像信息
     * @return yes/no
     */
    String updateAvatar(String token, Integer id, String base64);
}

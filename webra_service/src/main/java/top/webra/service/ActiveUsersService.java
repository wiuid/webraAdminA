package top.webra.service;

/**
 * @author webra
 */
public interface ActiveUsersService {
    /**
     * 查看在线人员
     * @return  在线人员列表
     */
    String getActiveUserList();

    /**
     * 在线人员踢下线
     * @param UUID  在线用户唯一编号
     * @return  yes/no
     */
    String deleteActiveUserList(String UUID);
}

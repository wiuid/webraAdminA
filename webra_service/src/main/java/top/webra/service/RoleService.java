package top.webra.service;

import top.webra.bean.ResponseBean;
import top.webra.pojo.Role;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:07
 * @Description: --
 */
public interface RoleService {

    /**
     * 检索角色
     * @param title             角色名
     * @param code              角色字符
     * @param state             角色状态
     * @param createDateStart   创建开始时间
     * @param createDateEnd     创建结束时间
     * @param page              页码
     */
    ResponseBean getRoleList(String title,
                             String code,
                             Integer state,
                             String createDateStart,
                             String createDateEnd,
                             Integer page);

    /**
     * 根据id 获取角色信息
     * @param id    角色id
     */
    ResponseBean getRole(Integer id);

    /**
     * 角色新建/修改处理
     * @param role  角色对象
     */
    ResponseBean saveRole(String token, Role role);

    /**
     * 根据id 修改状态
     * @param id    角色id
     */
    ResponseBean updateRoleSwitch(String token, Integer id);
    /**
     * 删除单个角色
     * @param id    角色id
     */
    ResponseBean deleteRole(String token, Integer id);

    /**
     * 批量删除角色
     * @param ids    角色ids列表字符串
     */
    ResponseBean deleteRoles(String token, String ids);


    /**
     * 角色列表，用以 选择
     */
    ResponseBean getRoleTree();

    void exportRoles(HttpServletResponse response);
}

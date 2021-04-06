package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.bean.ResponseBean;
import top.webra.pojo.Role;
import top.webra.service.impl.RoleServiceImpl;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:28
 * @Description: 用户权限管理接口
 */
@Controller
@ResponseBody
@RequestMapping("/system/user/role")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    /**
     * 所需参数：权限名称、权限字符、权限状态、时间、页码
     * @return 权限角色列表
     */
    @PreAuthorize("hasRole('ROLE_auth')")
    @GetMapping
    public ResponseBean getTableDate(String title, String code, Integer state, String createDateStart, String createDateEnd, Integer page){
        return roleService.getRoleList(title,code,state,createDateStart,createDateEnd,page);
    }

    /**
     * 根据id 拆线呢指定角色信息
     * @param id 角色id
     */
    @PreAuthorize("hasRole('ROLE_auth')")
    @GetMapping("/get")
    public ResponseBean getRole(Integer id){
        return roleService.getRole(id);
    }

    /**
     * 获取角色列表，用户选择角色
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @GetMapping("/tree")
    public ResponseBean getRoleTree(){
        return roleService.getRoleTree();
    }

    /**
     * 所需参数：权限对象
     * 同时处理新增、修改权限对象，区别点在权限id
     * @return 成功与否
     */
    @PreAuthorize("hasRole('ROLE_auth')")
    @PostMapping("/save")
    public ResponseBean saveRole(@RequestHeader("token") String token, Role role){
        return roleService.saveRole(token, role);
    }

    /**
     * 删除单个权限角色
     * @param id 角色id
     */
    @PreAuthorize("hasRole('ROLE_auth')")
    @DeleteMapping("/delete")
    public ResponseBean deleteRole(@RequestHeader("token") String token, int id){
        return roleService.deleteRole(token, id);
    }

    /**
     * 批量删除权限角色
     * @param ids 角色ids 列表字符串
     */
    @PreAuthorize("hasRole('ROLE_auth')")
    @DeleteMapping("/deletes")
    public ResponseBean deleteRoles(@RequestHeader("token") String token, String ids){
        return roleService.deleteRoles(token, ids);
    }

    /**
     * 单个权限角色开关
     * @param id 角色id
     */
    @PreAuthorize("hasRole('ROLE_auth')")
    @PostMapping("/state")
    public ResponseBean blockRole(@RequestHeader("token") String token, int id){
        return roleService.updateRoleSwitch(token, id);
    }


    /**
     * 导出权限角色
     */
    @PreAuthorize("hasRole('ROLE_auth')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        roleService.exportRoles(response);
    }
}

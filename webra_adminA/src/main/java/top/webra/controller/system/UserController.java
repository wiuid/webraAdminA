package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.bean.ResponseBean;
import top.webra.pojo.User;
import top.webra.service.impl.UserServiceImpl;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author webra
 * @Create 2021-03-02 9:29
 * @Description 用户管理接口
 */
@Controller
@ResponseBody
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    /**
     * @param departmentId      部门id
     * @param username          用户名
     * @param phone             手机号
     * @param state             状态
     * @param page              页码
     * @param createDateStart   创建起始范围
     * @param createDateEnd     创建结束范围
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @GetMapping
    public ResponseBean getTableDate(@RequestHeader("token") String token, Integer departmentId, String username,Integer phone, Integer state, String createDateStart, String createDateEnd, Integer page){
        return userService.getUserList(token, departmentId,username,phone,state,createDateStart,createDateEnd,page);
    }
    // 获取单用户信息
    @GetMapping("/get")
    @PreAuthorize("hasRole('ROLE_user')")
    public ResponseBean getUser(Integer id){
        return userService.selectUser(id);
    }

    // 获取全部用户名信息 用于选择
    @GetMapping("/tree")
    public ResponseBean getUserByNickname(){
        return userService.selectUserByNickname();
    }

    /**
     * 所需参数：用户对象
     * 该接口同时处理新建、修改用户，区分点是用户id是否为0
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @PostMapping("/save")
    public ResponseBean saveUser(@RequestHeader("token") String token, User user){
        return userService.saveUser(token, user);
    }

    // 修改用户密码
    @PreAuthorize("hasRole('ROLE_user')")
    @PostMapping("/password")
    public ResponseBean updatePassword(@RequestHeader("token") String token, Integer id, String rootPassword, String userPassword){
        return userService.updatePassword(token, id, rootPassword, userPassword);
    }

    // 修改用户状态
    @PreAuthorize("hasRole('ROLE_user')")
    @PostMapping("/state")
    public ResponseBean updateUserState(@RequestHeader("token") String token, Integer id){
        return userService.updateUserState(token, id);
    }

    /**
     * 删除单个用户
     * @param id 用户id
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @DeleteMapping("/delete")
    public ResponseBean deleteUser(@RequestHeader("token") String token, int id){
        return userService.deleteUser(token, id);
    }

    /**
     * 批量删除用户
     * @param ids 用户id列表字符串
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @DeleteMapping("/deletes")
    public ResponseBean deleteUsers(@RequestHeader("token") String token, String ids){
        return userService.deleteUsers(token, ids);
    }
}

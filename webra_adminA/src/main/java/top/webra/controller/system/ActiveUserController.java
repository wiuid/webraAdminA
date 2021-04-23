package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.webra.service.impl.ActiveUsersServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * @Author: webra
 * @Create: 2021-03-02 9:16
 * @Descriptio: 在线用户接口
 */
@Controller
@ResponseBody
@RequestMapping("/system/monitoring/user")
public class ActiveUserController {

    @Autowired
    private ActiveUsersServiceImpl activeUserService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_active')")
    public String getActiveUser(){
        return activeUserService.getActiveUserList();
    }
}

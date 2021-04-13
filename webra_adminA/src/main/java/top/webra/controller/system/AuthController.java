package top.webra.controller.system;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.webra.bean.ResponseBean;
import top.webra.service.impl.AuthServiceImpl;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/3/20 22:46
 * @Description: --
 */
@Controller
@ResponseBody
@RequestMapping("/system/user/auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;
    @PreAuthorize("hasRole('ROLE_auth')")
    @GetMapping("tree")
    public String getAuthTree(){
        return authService.getTree();
    }
}

package top.webra.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
@Api(tags = "权限")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;
    @PreAuthorize("hasRole('ROLE_auth')")
    @GetMapping("tree")
    @ApiOperation("获取全部权限信息")
    public String getAuthTree(){
        return authService.getTree();
    }
}

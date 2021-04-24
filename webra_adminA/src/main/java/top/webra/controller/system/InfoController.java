package top.webra.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.service.impl.InfoServiceImpl;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:29
 * @Description: 个人信息管理接口
 */
@Controller
@ResponseBody
@RequestMapping("/system/info")
@Api(tags = "个人信息管理")
public class InfoController {

    @Autowired
    private InfoServiceImpl infoService;
    /**
     * 不需要参数
     * 返回个人信息
     */
    @GetMapping
    @ApiOperation("个人信息")
    public String getTableDate(@RequestHeader("token") String token){
        return infoService.getInfo(token);
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/save")
    @ApiOperation("修改个人信息")
    public String editInfo(@RequestHeader("token") String token, Integer id, String nickname, String phone, String email){
        return infoService.setInfo(token,id,nickname,phone,email);
    }

    /**
     * 修改密码
     */
    @PostMapping("/password")
    @ApiOperation("修改密码")
    public String editPassword(@RequestHeader("token") String token, Integer id, String oldPassword, String newPassword){
        return infoService.setPassword(token,id,oldPassword,newPassword);
    }

    /** 修改头像 */
    @PostMapping("/avatar")
    @ApiOperation("修改头像")
    public String uploadAvatar(@RequestHeader("token") String token, Integer id, String base64){
        return infoService.updateAvatar(token, id, base64);
    }
    /** 获取头像 */
    @GetMapping("/avatar")
    @ApiOperation("查看头像")
    public String getAvatar(@RequestHeader("token") String token){
        return infoService.getAvatar(token);
    }

}

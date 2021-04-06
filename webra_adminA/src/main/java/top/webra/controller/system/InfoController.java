package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.bean.ResponseBean;
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
public class InfoController {

    @Autowired
    private InfoServiceImpl infoService;
    /**
     * 不需要参数
     * 返回个人信息
     */
    @GetMapping
    public ResponseBean getTableDate(@RequestHeader("token") String token){
        return infoService.getInfo(token);
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/save")
    public ResponseBean editInfo(@RequestHeader("token") String token, Integer id, String nickname, String phone, String email){
        return infoService.setInfo(token,id,nickname,phone,email);
    }

    /**
     * 修改密码
     */
    @PostMapping("/password")
    public ResponseBean editPassword(@RequestHeader("token") String token, Integer id, String oldPassword, String newPassword){
        return infoService.setPassword(token,id,oldPassword,newPassword);
    }

    // 修改头像
    @PostMapping("/avatar")
    public ResponseBean uploadAvatar(@RequestHeader("token") String token, Integer id, String base64){
        return infoService.updateAvatar(token, id, base64);
    }
    // 获取头像
    @GetMapping("/avatar")
    public ResponseBean getAvatar(@RequestHeader("token") String token){
        return infoService.getAvatar(token);
    }

}

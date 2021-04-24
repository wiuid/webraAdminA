package top.webra.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.webra.bean.ResponseBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 11:19
 * @Description: --
 */
@Controller
@ResponseBody
@RequestMapping("/index")
@Api(tags = "首页，非后台")
public class IndexController {
    @Autowired
    private ResponseBean responseBean;
    @GetMapping
    @ApiOperation(value = "/index首页接口")
    public ResponseBean index(){
        responseBean.setStatus(200);
        responseBean.setMsg("hello webra admin vue A");
        return responseBean;
    }
}

package top.webra.controller.system;

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
 * @Create: 2021-03-02 11:16
 * @Description: --
 */
@Controller
@ResponseBody
@RequestMapping("/system/about")
@Api(tags = "相关页")
public class AboutController {
    @Autowired
    private ResponseBean responseBean;

    @GetMapping
    @ApiOperation(value = "无意义")
    public ResponseBean getTableDate(){
        responseBean = ResponseBean.ok("success");
        return responseBean;
    }
}

package top.webra.controller.system;

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
public class AboutController {
    @Autowired
    private ResponseBean responseBean;

    @GetMapping
    public ResponseBean getTableDate(){
        responseBean = ResponseBean.ok("success");
        return responseBean;
    }
}

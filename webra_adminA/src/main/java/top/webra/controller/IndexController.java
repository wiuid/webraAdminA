package top.webra.controller;

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
public class IndexController {
    @Autowired
    private ResponseBean responseBean;
    @GetMapping
    public ResponseBean index(){
        responseBean.setStatus(200);
        responseBean.setMsg("hello webra admin vue A");
        return responseBean;
    }
}

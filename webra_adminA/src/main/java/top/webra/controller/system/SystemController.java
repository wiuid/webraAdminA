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
 * @Create: 2021-03-02 9:29
 * @Description: 首页信息接口
 */
@Controller
@ResponseBody
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private ResponseBean responseBean;

    /**
     * 不需要参数
     * 人员情况 1 2 3 4
     * 最近六条公告
     * 三个echarts图表信息
     */
    @GetMapping
    public ResponseBean getTableDate(){
        responseBean = ResponseBean.ok("success");
        return responseBean;
    }
}

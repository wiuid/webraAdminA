package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.webra.bean.ResponseBean;
import top.webra.service.impl.LogServiceImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:29
 * @Description: 日志管理接口
 */
@Controller
@ResponseBody
@RequestMapping("/system/site/log")
public class LogController {
    @Autowired
    private ResponseBean responseBean;

    @Autowired
    private LogServiceImpl logService;
    /**
     * 所需参数：日志标题、时间、分类、页码
     * @return 日志列表
     */
    @GetMapping
    public ResponseBean getTableDate(String title, String createDateStart, String createDateEnd, Integer page){
        return logService.getLogList(title,createDateStart,createDateEnd,page);
    }

    /**
     * 删除单个日志
     * @param id 日志id
     */
    @PostMapping("/delete")
    public ResponseBean deleteLog(int id){
        return responseBean;
    }

    /**
     * 批量删除日志
     * @param ids 日志ids列表字符串
     */
    @PostMapping("/deletes")
    public ResponseBean deleteLogs(List<Integer> ids){
        return responseBean;
    }
    /**
     * 清空日志
     */
    @PostMapping("/empty")
    public ResponseBean emptyLog(){
        return responseBean;
    }
    /**
     * 导出日志
     */
    @PostMapping("/export")
    public ResponseBean export(){
        return responseBean;
    }
}

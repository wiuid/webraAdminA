package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.bean.ResponseBean;
import top.webra.service.impl.LogServiceImpl;

import javax.servlet.http.HttpServletResponse;
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
@PreAuthorize("hasRole('ROLE_log')")
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
    @DeleteMapping("/delete")
    public ResponseBean deleteLog(int id){
        return logService.deleteLog(id);
    }

    /**
     * 批量删除日志
     * @param ids 日志ids列表字符串
     */
    @DeleteMapping("/deletes")
    public ResponseBean deleteLogs(@RequestHeader("token")String token, String ids){
        return logService.deleteLogs(token, ids);
    }
    /**
     * 清空日志
     */
    @DeleteMapping("/empty")
    public ResponseBean emptyLog(@RequestHeader("token")String token){
        return logService.emptyLog(token);
    }
    /**
     * 导出日志
     */
    @GetMapping("/export")
    public void export(@RequestHeader("token")String token, HttpServletResponse response){
        logService.exportLog(token, response);
    }
}

package top.webra.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.webra.bean.ResponseBean;
import top.webra.service.impl.MonitoringServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:29
 * @Description: 服务监控接口
 */
@Slf4j
@Controller
@ResponseBody
@RequestMapping("/system/monitoring")
@Api(tags = "服务监控")
public class MonitoringController {
    @Autowired
    private ResponseBean responseBean;

    @Autowired
    private MonitoringServiceImpl monitoringService;

    /**
     * 需要第三方组件 oshi
     * github：https://github.com/oshi/oshi
     * 中文教程：https://www.cnblogs.com/weechang/p/12493978.html
     * 中文教程：https://blog.csdn.net/only3c/article/details/90475327
     * @return 各类服务参数
     */
    @PreAuthorize("hasRole('ROLE_monitoring')")
    @GetMapping
    @ApiOperation("服务监控")
    public ResponseBean getTableDate(){
        Map<String, String> cpuInfo = monitoringService.getCpuInfo();
        Map<String, String> memoryInfo = monitoringService.getMemoryInfo();
        Map<String, String> diskInfo = monitoringService.getDiskInfo();
        Map<String, String> systemInfo = monitoringService.getSystemInfo();
        Map<String, String> jvmInfo = monitoringService.getJvmInfo();
        HashMap<String, Object> data = new HashMap<>(5);
        data.put("cpu",cpuInfo);
        data.put("memory",memoryInfo);
        data.put("disk",diskInfo);
        data.put("system",systemInfo);
        data.put("jvm",jvmInfo);
        responseBean.setData(data);
        responseBean.setStatus(200);
        responseBean.setMsg("success");
        return responseBean;
    }
}

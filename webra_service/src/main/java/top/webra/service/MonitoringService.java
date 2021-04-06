package top.webra.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-03 16:30
 * @Description: 系统监控
 */
public interface MonitoringService {

    /**
     * get cpu info
     */
    Map<String, String> getCpuInfo();

    /**
     * get memory info
     */
    Map<String, String> getMemoryInfo();
    /**
     * get disk info
     */
    Map<String, String> getDiskInfo();
    /**
     * get system info
     */
    Map<String, String> getSystemInfo();
    /**
     * get jvm info
     */
    Map<String, String> getJvmInfo();


}

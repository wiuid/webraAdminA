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
     * @return cpu信息
     */
    Map<String, String> getCpuInfo();

    /**
     * get memory info
     * @return 内存信息
     */
    Map<String, String> getMemoryInfo();
    /**
     * get disk info
     * @return 磁盘信息
     */
    Map<String, String> getDiskInfo();
    /**
     * get system info
     * @return 系统信息
     */
    Map<String, String> getSystemInfo();
    /**
     * get jvm info
     * @return jvm信息
     */
    Map<String, String> getJvmInfo();


}

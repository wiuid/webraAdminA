package top.webra.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;
import top.webra.service.MonitoringService;
import top.webra.util.CastUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-03 16:31
 * @Description: 监控逻辑业务类
 */
@Service
@ComponentScan
public class MonitoringServiceImpl implements MonitoringService {

    /**
     * map 中的内容分别是：逻辑处理器数、核心数、使用率、空闲率
     */
    @Override
    public Map<String, String> getCpuInfo() {
        HardwareAbstractionLayer hal = new SystemInfo().getHardware();
        HashMap<String, String> map = new HashMap<>(4);
        CentralProcessor cpu = hal.getProcessor();
        map.put("logicalProcessor", CastUtil.toString(cpu.getLogicalProcessorCount()));
        map.put("kernel", CastUtil.toString(cpu.getPhysicalProcessorCount()));

        long[] systemCpuLoadTicks = cpu.getSystemCpuLoadTicks();
        double systemCpuLoadBetweenTicks = cpu.getSystemCpuLoadBetweenTicks(systemCpuLoadTicks);
        DecimalFormat df = new DecimalFormat("0.00");
        map.put("usageRate", df.format(systemCpuLoadBetweenTicks));
        map.put("vacancyRate", df.format(1-systemCpuLoadBetweenTicks));

        return map;
    }

    /**
     * map 中的内容分别是：内存大小、已用内存大小、可用内存大小、使用率
     */
    @Override
    public Map<String, String> getMemoryInfo() {
        HardwareAbstractionLayer hal = new SystemInfo().getHardware();
        GlobalMemory memory = hal.getMemory();
        HashMap<String, String> map = new HashMap<>(4);
        String total = CastUtil.toString(memory.getTotal()/1024/1024);
        String available = CastUtil.toString(memory.getAvailable()/1024/1024);
        String current = CastUtil.toString(CastUtil.toLong(total)-memory.getAvailable()/1024/1024);
        DecimalFormat df = new DecimalFormat("0.00");
        String usageRate = df.format(CastUtil.toDouble(current) / CastUtil.toDouble(total));

        map.put("total", total);
        map.put("current", current);
        map.put("available", available);
        map.put("usageRate", usageRate);

        return map;
    }
    /**
     * map 中的内容分别是：磁盘信息
     */
    @Override
    public Map<String, String> getDiskInfo() {
        HardwareAbstractionLayer hal = new SystemInfo().getHardware();
        List<HWDiskStore> diskStores = hal.getDiskStores();
        HashMap<String, String> map = new HashMap<>(1);
        map.put("partitions", JSON.toJSONString(diskStores));
        return map;
    }
    /**
     * map 中的内容分别是：系统名、版本、运行时长、ip、系统名
     */
    @Override
    public Map<String, String> getSystemInfo() {
        OperatingSystem operatingSystem = new SystemInfo().getOperatingSystem();
        HashMap<String, String> map = new HashMap<>(5);
        map.put("system", operatingSystem.getFamily() + " - " + operatingSystem.getBitness());
        map.put("version", operatingSystem.getVersionInfo().toString());
        long systemUptime = operatingSystem.getSystemUptime();

        long d = systemUptime / (60 * 60 * 24);
        long h = (systemUptime - (60 * 60 * 24 * d)) / 3600;
        map.put("date", d + "天" + h + "时");

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            String hostname = localHost.getHostName();
            map.put("ip",ip);
            map.put("hostname",hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public Map<String, String> getJvmInfo() {

        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryMxBean = ManagementFactory.getMemoryMXBean();

        HashMap<String, String> map = new HashMap<>(4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = simpleDateFormat.format(new Date(runtimeMxBean.getStartTime()));
        map.put("date",format);
        map.put("version",runtimeMxBean.getSystemProperties().get("java.version"));
        map.put("memorySize",CastUtil.toString(memoryMxBean.getHeapMemoryUsage().getInit()/1024/1024));
        map.put("memoryMax",CastUtil.toString(memoryMxBean.getHeapMemoryUsage().getMax()/1024/1024));
        return map;
    }
}

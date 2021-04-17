package top.webra.adminA;

import org.junit.Test;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-03 14:12
 * @Description: oshi测试
 */
public class OshiTest {
    @Test
    public void oshiCpuTest(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        System.out.print("cpu最大频率    ");
        System.out.println(cpu.getMaxFreq());

        System.out.print("cpu当前频率    ");
        long[] currentFreq = cpu.getCurrentFreq();
        for (long freq : currentFreq){
            System.out.print(freq + "----");
        }

        System.out.println();
        System.out.print("cpu名称    ");
        CentralProcessor.ProcessorIdentifier processorIdentifier = cpu.getProcessorIdentifier();
        System.out.println(processorIdentifier.getName());

        long[] systemCpuLoadTicks = cpu.getSystemCpuLoadTicks();
        double systemCpuLoadBetweenTicks = cpu.getSystemCpuLoadBetweenTicks(systemCpuLoadTicks);
        System.out.println("cpu使用率    " + systemCpuLoadBetweenTicks);

        int logicalProcessorCount = cpu.getLogicalProcessorCount();
        System.out.println("cpu逻辑处理器的数量    " + logicalProcessorCount);
        System.out.println("cpu内核数量    " + cpu.getPhysicalProcessorCount());
        System.out.println("cpu插槽数量    " + cpu.getPhysicalPackageCount());
    }

    @Test
    public void oshiMemoryTest(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        GlobalMemory memory = hal.getMemory();
        // 单位：Bytes
        System.out.println("内存大小：" + memory.getTotal());
        System.out.println("可用内存大小：" + memory.getAvailable());
        System.out.println("内存分页大小：" + memory.getPageSize());
        System.out.println("物理内存信息：" + memory.getPhysicalMemory());
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        System.out.println("SwapPagesIn：" + virtualMemory.getSwapPagesIn());
        System.out.println("SwapPagesOut：" + virtualMemory.getSwapPagesOut());
        System.out.println("交换分区内存大小：" + virtualMemory.getSwapTotal());
        System.out.println("SwapUsed：" + virtualMemory.getSwapUsed());
        System.out.println("当前使用交换分区：" + virtualMemory.getVirtualInUse());
        System.out.println("最大可用交换分区：" + virtualMemory.getVirtualMax());
    }

    @Test
    public void oshiDisksTest(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        List<HWDiskStore> diskStores = hal.getDiskStores();

        System.out.println("磁盘数量：" + diskStores.size());
        HWDiskStore hwDiskStore = diskStores.get(0);
        System.out.println("磁盘名称：" + hwDiskStore.getName());
        System.out.println("磁盘模式：" + hwDiskStore.getModel());
        System.out.println("磁盘大小：" + hwDiskStore.getSize());
        System.out.println("（不支持mac）磁盘当前输入输出IO进展：" + hwDiskStore.getCurrentQueueLength());
        System.out.println("磁盘序列号：" + hwDiskStore.getSerial());
        System.out.println("磁盘分区信息：" + hwDiskStore.getPartitions());
        System.out.println("磁盘读取的字节数：" + hwDiskStore.getReadBytes());
        System.out.println("磁盘更新统计信息的时间：" + hwDiskStore.getTimeStamp());
        System.out.println("磁盘读取的次数：" + hwDiskStore.getReads());


    }
    @Test
    public void oshiSystemTest(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        ComputerSystem computerSystem = hal.getComputerSystem();

        Baseboard baseboard = computerSystem.getBaseboard();
        System.out.println("-----------------------------");
        System.out.println("主板信息如下：");
        System.out.println("主板制造商：" + baseboard.getManufacturer());
        System.out.println("主板模型：" + baseboard.getModel());
        System.out.println("主板序列号：" + baseboard.getSerialNumber());
        System.out.println("主板版本：" + baseboard.getVersion());

        Firmware firmware = computerSystem.getFirmware();
        System.out.println("-----------------------------");
        System.out.println("系统固件BIOS信息如下：");
        System.out.println("固件描述信息:" + firmware.getDescription());
        System.out.println("固件制造商:" + firmware.getManufacturer());
        System.out.println("固件名称:" + firmware.getName());
        System.out.println("固件发行日期:" + firmware.getReleaseDate());
        System.out.println("固件版本:" + firmware.getVersion());

        System.out.println("-----------------------------");
        String hardwareUUID = computerSystem.getHardwareUUID();
        String manufacturer = computerSystem.getManufacturer();
        String model = computerSystem.getModel();
        String serialNumber = computerSystem.getSerialNumber();
        System.out.println("硬件UUID:" + hardwareUUID);
        System.out.println("硬件制造商:" + manufacturer);
        System.out.println("计算机系统模型:" + model);
        System.out.println("计算机系统序列号:" + serialNumber);
    }

    @Test
    public void oshiSensorsTest(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        Sensors sensors = hal.getSensors();

        System.out.println("cpu温度：" + sensors.getCpuTemperature());
        System.out.println("cpu电压：" + sensors.getCpuVoltage());
        int[] fanSpeeds = sensors.getFanSpeeds();
        if (fanSpeeds.length!=0){
            System.out.println("风扇转速：");
            for (int fanSpeed : fanSpeeds){
                System.out.print(fanSpeed + "----");
            }
        }
    }
    @Test
    public void oshiWinTest(){
        SystemInfo si = new SystemInfo();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        System.out.println("系统位数：" + operatingSystem.getBitness());
        System.out.println("操作系统：" + operatingSystem.getFamily());
        System.out.println("操作系统制造商：" + operatingSystem.getManufacturer());
        System.out.println("操作系统版本信息：" + operatingSystem.getVersionInfo());
        System.out.println("操作系统启动的时间：" + operatingSystem.getSystemUptime());
        System.out.println("Unix引导时间：" + operatingSystem.getSystemBootTime());

    }
    @Test
    public void oshiNetworkTest(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String s = localHost.getHostAddress();
            String t = localHost.getHostName();//获得本机名称
            System.out.println(s);
            System.out.println(t);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void jvmTest(){
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        System.out.println("pid:"+runtimeMXBean.getName());
        System.out.println("启动时间:"+new Date(runtimeMXBean.getStartTime()));
        System.out.println("jdk版本:"+runtimeMXBean.getSystemProperties().get("java.version"));
        System.out.println("堆初始化大小:"+memoryMXBean.getHeapMemoryUsage().getInit());
        System.out.println("堆最大值:"+memoryMXBean.getHeapMemoryUsage().getMax());
        System.out.println("nonHeap初始值:"+memoryMXBean.getNonHeapMemoryUsage().getInit());
        System.out.println("NonHeap最大值:"+memoryMXBean.getNonHeapMemoryUsage().getMax());

    }
}












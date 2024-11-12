package me.sallyio.neuronutil.common;

import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Locale;

public class SystemInfo {

    private final OperatingSystemMXBean osBean;
    private final boolean isWindows;

    public SystemInfo() {
        this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        this.isWindows = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
    }

    // Get the CPU name (OS-dependent)
    public String getCpuName() {
        String cpuName = System.getenv("PROCESSOR_IDENTIFIER"); // Works on Windows

        if (cpuName == null || cpuName.isEmpty()) {
            // Attempt to read from /proc/cpuinfo (Linux)
            try (BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.toLowerCase().contains("model name")) {
                        cpuName = line.split(":")[1].trim();
                        break;
                    }
                }
            } catch (IOException ignored) {
                // Ignored if /proc/cpuinfo is unavailable or not readable
            }
        }

        // If still null, fallback to the system's OS name as a generic option
        if (cpuName == null || cpuName.isEmpty()) {
            cpuName = osBean.getName(); // Generic fallback
        }

        return cpuName != null ? cpuName : "Unknown CPU";
    }

    // Get CPU usage as a percentage (0.0 - 100.0)
    public double getCpuUsage() {
        return osBean.getCpuLoad() * 100;
    }

    // Get total RAM in MB
    public long getTotalRam() {
        return osBean.getTotalMemorySize() / (1024 * 1024);
    }

    // Get currently used RAM in MB
    public long getCurrentRamUsage() {
        long totalMemory = osBean.getTotalMemorySize();
        long freeMemory = osBean.getFreeMemorySize();
        return (totalMemory - freeMemory) / (1024 * 1024);
    }

    // Get disk total size in GB for the root or main drive
    public long getTotalDiskSpace() {
        File root = isWindows ? new File("C:") : new File("/");
        return root.getTotalSpace() / (1024 * 1024 * 1024);
    }

    // Get used disk space in GB for the root or main drive
    public long getUsedDiskSpace() {
        File root = isWindows ? new File("C:") : new File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        return (totalSpace - freeSpace) / (1024 * 1024 * 1024);
    }

    // Get current process ID
    public long getCurrentPid() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }
}
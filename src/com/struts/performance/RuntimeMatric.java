package com.struts.performance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.util.StringTokenizer;

import com.sun.management.OperatingSystemMXBean;

public class RuntimeMatric implements Monitor {
	/** ��ʹ���ڴ�. */
	private long totalMemory;
	/** ʣ���ڴ�. */
	private long freeMemory;
	/** ����ʹ���ڴ�. */
	private long maxMemory;
	/** ����ϵͳ. */
	private String osName;
	/** �ܵ������ڴ�. */
	private long totalMemorySize;
	/** ʣ��������ڴ�. */
	private long freePhysicalMemorySize;
	/** ��ʹ�õ������ڴ�. */
	private long usedMemory;
	/** �߳�����. */
	private int totalThread;
	/** cpuʹ����. */
	private double cpuRatio;
	/** memʹ����. */
	private double memRatio;

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getTotalMemorySize() {
		return totalMemorySize;
	}

	public void setTotalMemorySize(long totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
	}

	public int getTotalThread() {
		return totalThread;
	}

	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public double getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	public double getMemRatio() {
		return memRatio;
	}

	public void setMemRatio(double memRatio) {
		this.memRatio = memRatio;
	}

	private static final int CPUTIME = 30;
	private static final int PERCENT = 100;
	private static final int FAULTLENGTH = 10;
	private static String linuxVersion = null;

	public static RuntimeMatric getMonitorMatric() {
		int kb = 1024;
		// ��ʹ���ڴ�
		long totalMemory = Runtime.getRuntime().totalMemory() / kb;
		// ʣ���ڴ�
		long freeMemory = Runtime.getRuntime().freeMemory() / kb;
		// ����ʹ���ڴ�
		long maxMemory = Runtime.getRuntime().maxMemory() / kb;
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		// ����ϵͳ
		String osName = System.getProperty("os.name");
		// �ܵ������ڴ�
		long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
		// ʣ��������ڴ�
		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
		// ��ʹ�õ������ڴ�
		long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb
				.getFreePhysicalMemorySize()) / kb;
		// ����߳�����
		ThreadGroup parentThread;
		for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
				.getParent() != null; parentThread = parentThread.getParent())
			;
		int totalThread = parentThread.activeCount();
		double cpuRatio = 0;
		if (osName.toLowerCase().startsWith("windows")) {
			cpuRatio = getCpuRatioForWindows();
		} else {
			cpuRatio = getCpuRateForLinux();
		}

		// ���췵�ض���
		RuntimeMatric infoBean = new RuntimeMatric();
		infoBean.setFreeMemory(freeMemory);
		infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);
		infoBean.setMaxMemory(maxMemory);
		infoBean.setOsName(osName);
		infoBean.setTotalMemory(totalMemory);
		infoBean.setTotalMemorySize(totalMemorySize);
		infoBean.setTotalThread(totalThread);
		infoBean.setUsedMemory(usedMemory);
		infoBean.setCpuRatio(cpuRatio);
		double memRatio = (infoBean.getUsedMemory() * 100 / infoBean
				.getTotalMemorySize());
		infoBean.setMemRatio(memRatio);
		return infoBean;
	}

	private static double getCpuRateForLinux() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		try {
			System.out.println("Get usage rate of CUP , linux version: "
					+ linuxVersion);
			Process process = Runtime.getRuntime().exec("top -b -n 1");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);
			if (linuxVersion.equals("2.4")) {
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				String user = tokenStat.nextToken();
				tokenStat.nextToken();
				String system = tokenStat.nextToken();
				tokenStat.nextToken();
				String nice = tokenStat.nextToken();
				System.out.println(user + " , " + system + " , " + nice);
				user = user.substring(0, user.indexOf("%"));
				system = system.substring(0, system.indexOf("%"));
				nice = nice.substring(0, nice.indexOf("%"));
				float userUsage = new Float(user).floatValue();
				float systemUsage = new Float(system).floatValue();
				float niceUsage = new Float(nice).floatValue();
				return (userUsage + systemUsage + niceUsage) / 100;
			} else {
				brStat.readLine();
				brStat.readLine();
				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				String cpuUsage = tokenStat.nextToken();
				System.out.println("CPU idle : " + cpuUsage);
				Float usage = new Float(cpuUsage.substring(0,
						cpuUsage.indexOf("%")));
				return (1 - usage.floatValue() / 100);
			}
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			freeResource(is, isr, brStat);
			return 1;
		} finally {
			freeResource(is, isr, brStat);
		}
	}

	private static void freeResource(InputStream is, InputStreamReader isr,
			BufferedReader br) {
		try {
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	private static double getCpuRatioForWindows() {
		try {
			String procCmd = System.getenv("windir")
					+ "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			// ȡ������Ϣ
			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return Double.valueOf(
						PERCENT * (busytime) / (busytime + idletime))
						.doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0.0;
		}
	}

	private static long[] readCpu(final Process proc) {
		long[] retn = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FAULTLENGTH) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}
				// �ֶγ���˳��Caption,CommandLine,KernelModeTime,ReadOperationCount,
				// ThreadCount,UserModeTime,WriteOperation
				String caption = Bytes.substring(line, capidx, cmdidx - 1)
						.trim();
				String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				String s1 = Bytes.substring(line, kmtidx, rocidx - 1).trim();
				String s2 = Bytes.substring(line, umtidx, wocidx - 1).trim();
				if (caption.equals("System Idle Process")
						|| caption.equals("System")) {
					if (s1.length() > 0)
						idletime += Long.valueOf(s1).longValue();
					if (s2.length() > 0)
						idletime += Long.valueOf(s2).longValue();
					continue;
				}
				if (s1.length() > 0)
					kneltime += Long.valueOf(s1).longValue();
				if (s2.length() > 0)
					usertime += Long.valueOf(s2).longValue();
			}
			retn[0] = idletime;
			retn[1] = kneltime + usertime;
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

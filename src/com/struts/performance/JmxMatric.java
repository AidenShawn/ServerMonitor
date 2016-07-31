package com.struts.performance;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxMatric implements Monitor {

	private double hcommitted;

	private double hinit;

	private double hmax;

	private double hused;

	private double noncommitted;

	private double noninit;

	private double nonmax;

	private double nonused;

	private double memratio;

	private double cpuratio;

	public static JmxMatric getJMXMonitorMatric(final String service) {
		JmxMatric result = new JmxMatric();
		
		if(service == null || service.equals("")) {
			return result;
		}

		JMXConnector jmxConnector = null;
		try {
			JMXServiceURL ServiceURL = new JMXServiceURL(service);
			jmxConnector = JMXConnectorFactory.connect(ServiceURL);
			// MBean 服务器（无论是本地的还是远程的）进行通信的一种方式 MBeanServerConnection
			MBeanServerConnection mBeanServerConnection = jmxConnector
					.getMBeanServerConnection();

			// 获取MemoryMXBean
			MemoryMXBean memoryMXBean = ManagementFactory
					.newPlatformMXBeanProxy(mBeanServerConnection,
							ManagementFactory.MEMORY_MXBEAN_NAME,
							MemoryMXBean.class);

			MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

			result.setHcommitted(heapMemoryUsage.getCommitted());
			result.setHinit(heapMemoryUsage.getInit());
			result.setHmax(heapMemoryUsage.getMax());
			result.setHused(heapMemoryUsage.getUsed());

			MemoryUsage nonHeapMemoryUsage = memoryMXBean
					.getNonHeapMemoryUsage();
			result.setNoncommitted(nonHeapMemoryUsage.getCommitted());
			result.setNoninit(nonHeapMemoryUsage.getInit());
			result.setNonmax(nonHeapMemoryUsage.getMax());
			result.setNonused(nonHeapMemoryUsage.getUsed());

			double memratio = 0.0;
			memratio = heapMemoryUsage.getUsed() / heapMemoryUsage.getInit();

			OperatingSystemMXBean operatingSystemMXBean = ManagementFactory
					.newPlatformMXBeanProxy(mBeanServerConnection,
							ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
							OperatingSystemMXBean.class);
			double cpuratio = 0.0;
			long start = System.currentTimeMillis();
			long startC;
			try {
				startC = (Long) mBeanServerConnection
						.getAttribute(operatingSystemMXBean.getObjectName(),
								"ProcessCpuTime");
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				long end = System.currentTimeMillis();
				long endC = (Long) mBeanServerConnection
						.getAttribute(operatingSystemMXBean.getObjectName(),
								"ProcessCpuTime");

				int availableProcessors = operatingSystemMXBean
						.getAvailableProcessors();
				cpuratio = (endC - startC) / 1000000.0 / (end - start)
						/ availableProcessors;
				result.setCpuratio(cpuratio);
				result.setMemratio(memratio);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (MalformedURLException e) {
			System.out.println("illegal ServiceURL: " + service);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (jmxConnector != null) {
					jmxConnector.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public double getHcommitted() {
		return hcommitted;
	}

	public void setHcommitted(double hcommitted) {
		this.hcommitted = hcommitted;
	}

	public double getHinit() {
		return hinit;
	}

	public void setHinit(double hinit) {
		this.hinit = hinit;
	}

	public double getHmax() {
		return hmax;
	}

	public void setHmax(double hmax) {
		this.hmax = hmax;
	}

	public double getHused() {
		return hused;
	}

	public void setHused(double hused) {
		this.hused = hused;
	}

	public double getNoncommitted() {
		return noncommitted;
	}

	public void setNoncommitted(double noncommitted) {
		this.noncommitted = noncommitted;
	}

	public double getNoninit() {
		return noninit;
	}

	public void setNoninit(double noninit) {
		this.noninit = noninit;
	}

	public double getNonmax() {
		return nonmax;
	}

	public void setNonmax(double nonmax) {
		this.nonmax = nonmax;
	}

	public double getNonused() {
		return nonused;
	}

	public void setNonused(double nonused) {
		this.nonused = nonused;
	}

	public double getMemRatio() {
		return memratio;
	}

	public void setMemratio(double memratio) {
		this.memratio = memratio;
	}

	public double getCpuRatio() {
		return cpuratio;
	}

	public void setCpuratio(double cpuratio) {
		this.cpuratio = cpuratio;
	}

}

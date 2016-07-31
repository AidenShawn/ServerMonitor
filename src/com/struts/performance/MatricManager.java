package com.struts.performance;

import java.util.ArrayList;
import java.util.List;

public class MatricManager {

	private static MatricManager MANAGER = new MatricManager();

	private String ip = "";

	private String port = "";

	private final List<Monitor> list = new ArrayList<Monitor>();

	private String service = "";

	private static final String SERVICE = "service:jmx:rmi:///jndi/rmi://<ip>:<port>/jmxrmi";

	public static MatricManager getMatricManager() {
		if (MANAGER == null) {
			MANAGER = new MatricManager();
		}

		return MANAGER;
	}

	public List<Monitor> collect() {
		Monitor mc = JmxMatric.getJMXMonitorMatric(this.service);
		list.add(mc);
		return list;
	}

	public List<Monitor> collect2() {
		Monitor mc = RuntimeMatric.getMonitorMatric();
		list.add(mc);
		return list;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
		setService();
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
		setService();
	}

	private void setService() {
		if (this.service.equals("")) {
			this.service = SERVICE;
		}
		if (!this.ip.equals("")) {
			this.service = this.service.replaceFirst("<ip>", this.ip);
		}
		if (!this.port.equals("")) {
			this.service = this.service.replaceFirst("<port>", this.port);
		}
	}

}

package com.struts;

import org.apache.struts.action.ActionForm;

public class InputActionForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3309083957573776686L;

	private String ip;
	private String port;

	public void setPort(String port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

}

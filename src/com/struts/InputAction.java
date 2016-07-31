package com.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.struts.performance.MatricManager;

public class InputAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InputActionForm laf = (InputActionForm) form;
		String ip = laf.getIp();
		String port = laf.getPort();

		MatricManager.getMatricManager().setIp(ip);
		MatricManager.getMatricManager().setPort(port);

		try {
			request.setAttribute("ip", ip);
			return mapping.findForward("success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("error");
	}
}

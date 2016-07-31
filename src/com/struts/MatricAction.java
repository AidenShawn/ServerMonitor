package com.struts;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.struts.performance.MatricManager;
import com.struts.performance.Monitor;

public class MatricAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		JSONArray jsonArray = new JSONArray();
		// for (Monitor m : MatricManager.getMatricManager().collect()) {
		for (Monitor m : MatricManager.getMatricManager().collect2()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cpuratio", m.getCpuRatio());
			jsonObject.put("memratio", m.getMemRatio());
			jsonArray.add(jsonObject);
		}
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonArray);

		return null;
	}
}

package com.en.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.en.handler.AjaxBaseHandler;

public class AjaxServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sHandler = request.getParameter(HANDLERS)!= null ? request.getParameter(HANDLERS) : "";
		response.setContentType("application/json");
		JSONObject result = null;
		AjaxBaseHandler handler = (AjaxBaseHandler) pageMap.get(sHandler);
        result = handler.handleAjaxRequest(request);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
	}

}

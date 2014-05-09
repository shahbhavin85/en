package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public abstract class AjaxBaseHandler extends Basehandler {

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	public abstract JSONObject handleAjaxRequest(HttpServletRequest request);

}

package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.Message;
import com.en.model.MessageModel;

public class LogoutHandler extends Basehandler {

	public static String getPageName() {
		return LOGOUT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		// action to taken
		request.getSession().removeAttribute(USER);
		request.getSession().removeAttribute(ACCESS_POINT);
		MessageModel msg = new MessageModel();
		msg.addNewMessage(new Message(SUCCESS, "Logged out successfully."));
		request.setAttribute(MESSAGES, msg);
		return (new LoginHandler()).handlerRequest(request, response);
	}
}

package com.en.handler;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.MessageModel;

public class HomeSettingHandler extends Basehandler {

	public static String getPageName() {
		return HOME_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "homeSettings.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(UPDATE_HOME)){
			updateHome(request,response);			
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void updateHome(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> homeDtls = new HashMap<String, String>();
		if(request.getParameter(THOUGHT) != null)
			homeDtls.put(THOUGHT, request.getParameter(THOUGHT));
		if(request.getParameter(GOOD_NEWS) != null)
			homeDtls.put(GOOD_NEWS, request.getParameter(GOOD_NEWS));
		if(request.getParameter(JACKPOT) != null)
			homeDtls.put(JACKPOT, request.getParameter(JACKPOT));
		MessageModel model = getHomeSettingBroker().updateHomeSettingDtls(homeDtls);
		request.setAttribute(MESSAGES, model);
		initialize(request);
		return;
	}

	private void initialize(HttpServletRequest request) {
		HashMap<String, String> homeSetting = getHomeSettingBroker().getHomeDetails();
		if(homeSetting != null){
			Iterator<String> itr = homeSetting.keySet().iterator();
			String key = "";
			while (itr.hasNext()){
				key = itr.next();
				request.setAttribute(key, homeSetting.get(key));
			}
		}
		return;
	}

}

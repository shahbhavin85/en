package com.en.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.UserModel;


public class LoginHandler extends Basehandler {

	public static String getPageName() {
		return LOGIN_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "login.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(CHECK_ACCESS)){
			pageType = checkAccess(request,response);			
		} else if(strAction.equals(INIT_PASSWORD)){
			pageType = "changePass.jsp";			
		} else if(strAction.equals(CHANGE_ACCESS_POINT)){
			pageType = changeAccessPoint(request,response);
		} else if(strAction.equals(CHANGE_PASSWORD)){
			pageType = changePassword(request, response);			
		} else {
			pageType = initialize(request);
		}
		return pageType;
	}

	private String changeAccessPoint(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "";
		UserModel user = (UserModel) request.getSession().getAttribute(USER);
		String userId = user.getUserId();
		String pwd = user.getPassword();
		String accessPoint = request.getParameter("changeAccessPoint");
		user = getLoginBroker().checkAccess(userId, pwd, accessPoint);
		if(user == null){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "Username and password combination is incorrect (or) username doesnot have access to access point. Please enter the valid details."));
			request.setAttribute(MESSAGES, msg);
			pageType = "login.jsp";
			initialize(request);
		} else {
			request.getSession().setAttribute(USER, user);
			request.getSession().setAttribute(ACCESS_POINT, accessPoint);
			AccessPointModel[] accessLst = getLoginBroker().getUserAccessList(user);
			request.getSession().setAttribute(USERS_ACCESS_LIST, accessLst);
			AccessPointModel access = getAccessPointBroker().getAccessPointDtls(Integer.parseInt(accessPoint));
			if(accessPoint.equals("1")){
				access.setAccessId(1);
			}
			if(accessPoint.equals("3")){
				access.setAccessId(3);
			}
			if(accessPoint.equals("2")){
				access.setAccessId(2);
			}
			request.getSession().setAttribute(ACCESS_POINT_DTLS, access);
			ItemModel[] items = getItemBroker().getUpdatedItems();
			request.setAttribute(UPDATED_ITEMS, items);
			String version = getItemBroker().getPriceListVersion();
			request.setAttribute(PL_VERSION, version);
			HashMap<String, String> homeSetting = getHomeSettingBroker().getHomeDetails();
			if(homeSetting != null){
				Iterator<String> itr = homeSetting.keySet().iterator();
				String key = "";
				while (itr.hasNext()){
					key = itr.next();
					request.setAttribute(key, homeSetting.get(key));
				}
			}
			if(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) == 3 || Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) == 2 || Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) > 10){
				ActionHandler.getOpeninfBalance(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)), request);
			}
			ActionHandler.getPendingRequestnDemand(request);
//			EmailUtil.sendImgMail(new String[] {"shahbhavin85@gmail.com","heshstorebhavin@hotmail.com"}, "test", "");
			pageType = "userhome.jsp";
//			pageType = (new BillHandler()).handlerRequest(request, response);
		}
		return pageType;
	}

	private String initialize(HttpServletRequest request) {
		String pageType = "login.jsp";
		ArrayList<AccessPointModel> accessPoints = getLoginBroker().getAccessPoints();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		if(request.getSession().getAttribute(USER) != null && request.getSession().getAttribute(ACCESS_POINT) != null && !((String)request.getSession().getAttribute(ACCESS_POINT)).equals("")){
			UserModel user = getLoginBroker().checkAccess(((UserModel)request.getSession().getAttribute(USER)).getUserId(), ((UserModel)request.getSession().getAttribute(USER)).getPassword(), (String)request.getSession().getAttribute(ACCESS_POINT));
			if(user != null){
				request.getSession().setAttribute(USER, user);
				request.getSession().setAttribute(ACCESS_POINT, request.getSession().getAttribute(ACCESS_POINT));
				AccessPointModel[] accessLst = getLoginBroker().getUserAccessList(user);
				request.getSession().setAttribute(USERS_ACCESS_LIST, accessLst);
				AccessPointModel access = getAccessPointBroker().getAccessPointDtls(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
				if(request.getSession().getAttribute(ACCESS_POINT).equals("1")){
					access.setAccessId(1);
				}
				if(request.getSession().getAttribute(ACCESS_POINT).equals("3")){
					access.setAccessId(3);
				}
				if(request.getSession().getAttribute(ACCESS_POINT).equals("2")){
					access.setAccessId(2);
				}
				request.getSession().setAttribute(ACCESS_POINT_DTLS, access);
				ItemModel[] items = getItemBroker().getUpdatedItems();
				request.setAttribute(UPDATED_ITEMS, items);
				String version = getItemBroker().getPriceListVersion();
				request.setAttribute(PL_VERSION, version);
				HashMap<String, String> homeSetting = getHomeSettingBroker().getHomeDetails();
				if(homeSetting != null){
					Iterator<String> itr = homeSetting.keySet().iterator();
					String key = "";
					while (itr.hasNext()){
						key = itr.next();
						request.setAttribute(key, homeSetting.get(key));
					}
				}
				if(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) == 2 || Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) > 10){
					ActionHandler.getOpeninfBalance(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)), request);
				}
				ActionHandler.getPendingRequestnDemand(request);
				pageType = "userhome.jsp";
			} else {
				request.getSession().removeAttribute(USER);
				request.getSession().removeAttribute(ACCESS_POINT);
				request.getSession().removeAttribute(ACCESS_POINT_DTLS);				
			}
		} 
		return pageType;
	}

	private String changePassword(HttpServletRequest request,
			HttpServletResponse response) {
		String pageName = "changePass.jsp";
//		String userId = request.getParameter("userId");
//		String oldPwd = request.getParameter("oldPwd");
//		String newPwd = request.getParameter("newPwd1");
//		if(getLoginBroker().checkAccess(userId, oldPwd)){
//			if(getLoginBroker().changePassword(userId, oldPwd, newPwd)){
//				request.setAttribute("msg", "PASSWORD CHANGED SUCCESSFULLY.");
//			} else {
//				request.setAttribute("msg", "ERROR OCCURED WHILE CHANGING THE PASSWORD.");
//			}
//		} else {
//			request.setAttribute("msg", "USERNAME AND PASSWORD COMBINATION IS INCORRECT. PLEASE INSERT THE CORRECT COMBINATION.");
//		}
		return pageName;
	}

	private String checkAccess(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "";
		String userId = request.getParameter("txtUser");
		String pwd = request.getParameter("txtPwd");
		String accessPoint = request.getParameter("sAccessPoint");
		UserModel user = getLoginBroker().checkAccess(userId, pwd, accessPoint);
		if(user == null){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "Username and password combination is incorrect (or) username doesnot have access to access point. Please enter the valid details."));
			request.setAttribute(MESSAGES, msg);
			pageType = "login.jsp";
			initialize(request);
		} else if(request.getSession().getAttribute(USER) != null && request.getSession().getAttribute(ACCESS_POINT) != null && !((String)request.getSession().getAttribute(ACCESS_POINT)).equals("")){
			pageType = initialize(request);
		} else {
			request.getSession().setAttribute(USER, user);
			request.getSession().setAttribute(ACCESS_POINT, accessPoint);
			AccessPointModel[] accessLst = getLoginBroker().getUserAccessList(user);
			request.getSession().setAttribute(USERS_ACCESS_LIST, accessLst);
			AccessPointModel access = getAccessPointBroker().getAccessPointDtls(Integer.parseInt(accessPoint));
			if(accessPoint.equals("1")){
				access.setAccessId(1);
			}
			if(accessPoint.equals("3")){
				access.setAccessId(3);
			}
			if(accessPoint.equals("2")){
				access.setAccessId(2);
			}
			request.getSession().setAttribute(ACCESS_POINT_DTLS, access);
			ItemModel[] items = getItemBroker().getUpdatedItems();
			request.setAttribute(UPDATED_ITEMS, items);
			String version = getItemBroker().getPriceListVersion();
			request.setAttribute(PL_VERSION, version);
			HashMap<String, String> homeSetting = getHomeSettingBroker().getHomeDetails();
			if(homeSetting != null){
				Iterator<String> itr = homeSetting.keySet().iterator();
				String key = "";
				while (itr.hasNext()){
					key = itr.next();
					request.setAttribute(key, homeSetting.get(key));
				}
			}
			if(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) == 3 || Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) == 2 || Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) > 10){
				ActionHandler.getOpeninfBalance(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)), request);
			}
			ActionHandler.getPendingRequestnDemand(request);
//			EmailUtil.sendImgMail(new String[] {"shahbhavin85@gmail.com","heshstorebhavin@hotmail.com"}, "test", "");
			pageType = "userhome.jsp";
//			pageType = (new BillHandler()).handlerRequest(request, response);
		}
		return pageType;
	}

}

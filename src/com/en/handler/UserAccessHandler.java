package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.MessageModel;
import com.en.model.UserModel;

public class UserAccessHandler extends Basehandler {

	public static String getPageName() {
		return USER_ACCESS_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "userAccess.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(UPDT_USER_ACCESS)){
			updateUserAccess(request);			
		} else if(strAction.equals(GET_USER_ACCESS)){
			getUserAccess(request);			
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void updateUserAccess(HttpServletRequest request) {
		UserModel model = populateDataModel(request);
		MessageModel msgModel = getUserAccessBroker().updateUserAccess(model);
		request.setAttribute(MESSAGES, msgModel);
		model = getUserAccessBroker().getUserAccessDtls(model.getUserId());
		request.setAttribute(FORM_DATA, model);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

	private void getUserAccess(HttpServletRequest request) {
		UserModel model = getUserAccessBroker().getUserAccessDtls(request.getParameter("txtUserId"));
		request.setAttribute(FORM_DATA, model);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

	private void initialize(HttpServletRequest request) {
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

	private UserModel populateDataModel(HttpServletRequest request) {
		UserModel model = new UserModel();
		if(request.getParameter("txtUserId") != null && !request.getParameter("txtUserId").trim().equals(""))
			model.setUserId(request.getParameter("txtUserId"));
		if(request.getParameter("txtAccessNos") != null && !request.getParameter("txtAccessNos").trim().equals("0")){
			int iLen = Integer.parseInt(request.getParameter("txtAccessNos"));
			if(request.getParameter("r"+0).equals("Y")){			
				model.addAccessPoints(0+"");			
				model.addAccessPoints(1+"");			
				model.addAccessPoints(2+"");			
				model.addAccessPoints(3+"");
				for(int i=11; i<iLen+10; i++){		
					if(request.getParameter("r"+i) != null)
						model.addAccessPoints(i+"");
				}
			} else {
				if(request.getParameter("r"+1).equals("Y")){		
					model.addAccessPoints(1+"");
				}
				if(request.getParameter("r"+2).equals("Y")){		
					model.addAccessPoints(2+"");
				}
				if(request.getParameter("r"+3).equals("Y")){		
					model.addAccessPoints(3+"");
				} 
				for(int i=11; i<iLen+10; i++){
					if(request.getParameter("r"+i)!=null && request.getParameter("r"+i).equals("Y")){				
						model.addAccessPoints(i+"");
					}
				}
			}
		}
		
		return model;
	}

}

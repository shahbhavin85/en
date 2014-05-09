package com.en.handler;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AttendanceModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.UserModel;

public class AttendanceHandler extends Basehandler {

	public static String getPageName() {
		return ATTENDANCE_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "initAttendance.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_ATTENDANCE)){
        	 pageType = getAttendanceDtls(request);			
		} else if(strAction.equals(UPDATE_ATTENDANCE)){
			pageType = updateAttendance(request);
			//pageType = "modifyUser.jsp";			
//		} else if(strAction.equals(UPDT_USER)){
//			updateUser(request);
//			pageType = "modifyUser.jsp";			
//		} else if(strAction.equals(GET_USER)){
//			getUser(request);
//			pageType = "modifyUser.jsp";			
//		} else if(strAction.equals(INIT_PASSWORD)){
//			initialize(request);
//			pageType = "resetPassword.jsp";
//		} else if(strAction.equals(INIT_USER_DOC)){
//			initialize(request);
//			pageType = "userDocument.jsp";
//		} else if(strAction.equals(GET_USER_DOC)){
//			getUserDocumentList(request);
//			initialize(request);
//			pageType = "userDocument.jsp";
//		} else if(strAction.equals(UPLOAD_USER_DOC)){
//			uploadUserDocumentList(list,request);
//			initialize(request);
//			pageType = "userDocument.jsp";
//		} else if(strAction.equals(UPDT_PASSWORD)){
//			updatePassword(request);
//			initialize(request);
//			pageType = "resetPassword.jsp";
//		} else {
//			initialize(request);
		}
		return pageType;
	}

	private String updateAttendance(HttpServletRequest request) {
		String pageName = "attendance.jsp";
		String[] users = request.getParameterValues("users");
		String userId = ((UserModel)request.getSession().getAttribute(USER)).getUserId();
		String attDate = request.getParameter("txtAttDate");
		ArrayList<AttendanceModel> lst = new ArrayList<AttendanceModel>();
		AttendanceModel temp = null;
		for(int i=0; i<users.length; i++){
				temp = new AttendanceModel();
				temp.getUser().setUserId(users[i]);
				if(!request.getParameter("att"+users[i]).equals("--")){
					temp.setType(request.getParameter("att"+users[i]));
					temp.getUser().setType(Integer.parseInt(request.getParameter("type"+users[i])));
					temp.setOt(request.getParameter("ot"+users[i]));
					temp.setInTime(request.getParameter("inTime"+users[i]));
					temp.setOutTime(request.getParameter("outTime"+users[i]));
					temp.setLateFine(request.getParameter("lateFine"+users[i]));
					temp.setPanelty(request.getParameter("panelty"+users[i]));
					temp.setRemarks(request.getParameter("remark"+users[i]));
				}
				lst.add(temp);
		}
		MessageModel msg = getAttendanceBroker().updateAttendance(attDate, lst);
		request.setAttribute(MESSAGES, msg);
		AttendanceModel[] attendance = getAttendanceBroker().getAttendanceDtls(userId,attDate);
		request.setAttribute("txtAttDate", attDate);
		request.setAttribute(FORM_DATA, attendance);
		return pageName;
	}

	private String getAttendanceDtls(HttpServletRequest request) {
		String pageName = "attendance.jsp";
		String userId = ((UserModel)request.getSession().getAttribute(USER)).getUserId();
		String attDate = request.getParameter("txtAttDate");
		AttendanceModel[] attendance = getAttendanceBroker().getAttendanceDtls(userId,attDate);
		request.setAttribute("txtAttDate", attDate);
		if(attendance.length==0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No user under you."));
			request.setAttribute(MESSAGES, msg);
			pageName = "initAttendance.jsp"; 
		} else {
			request.setAttribute(FORM_DATA, attendance);
		}
		return pageName;
	}

}

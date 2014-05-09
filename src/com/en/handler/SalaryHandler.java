package com.en.handler;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.EntryModel;
import com.en.model.MessageModel;
import com.en.model.SalaryModel;
import com.en.model.TargetModel;
import com.en.model.UserModel;

public class SalaryHandler extends Basehandler {

	public static String getPageName() {
		return SALARY_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType ="salary.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_SALARY_DETAILS)){
        	pageType = getSalaryDetails(request);			
		} else  if(strAction.equals(INIT_SALARY_RPT)){
        	pageType = initializeSalaryRpt(request);			
		} else  if(strAction.equals(SAVE_STAFF_SALARY_DETAILS)){
        	pageType = saveStaffSalary(request);			
		} else  if(strAction.equals(GET_STAFF_SALARY_DETAILS)){
        	pageType = getStaffSalaryDetails(request);			
		} else  if(strAction.equals(GET_SALARY_RPT)){
        	pageType = getSalaryDetails(request);			
		}
		return pageType;
	}

	private String saveStaffSalary(HttpServletRequest request) {
		UserModel user = (UserModel) getUserBroker().getUserDtls(request.getParameter("txtUserId"));
		int month = Integer.parseInt(request.getParameter("sMonth"));
		int year = Integer.parseInt(request.getParameter("sYear"));
		SalaryModel model = getUserBroker().getSalaryDetails(user, month, year);
		model.setUser(user);
		model.setExpense1(Double.parseDouble(request.getParameter("txtExpense1")));
		model.setExpense2(Double.parseDouble(request.getParameter("txtExpense2")));
		model.setExpense3(Double.parseDouble(request.getParameter("txtExpense3")));
		model.setExpense4(Double.parseDouble(request.getParameter("txtExpense4")));
		model.setExpense5(Double.parseDouble(request.getParameter("txtExpense5")));
		model.setAdvanceAdjust(Double.parseDouble(request.getParameter("txtAdvances")));
		model.setPaidOn(request.getParameter("txtPaidOn"));
		MessageModel msg = getUserBroker().saveStaffSalary(model);
		request.setAttribute(MESSAGES, msg);
		return getStaffSalaryDetails(request);
	}

	private String getStaffSalaryDetails(HttpServletRequest request) {
		UserModel user = (UserModel) getUserBroker().getUserDtls(request.getParameter("txtUserId"));
		int month = Integer.parseInt(request.getParameter("sMonth"));
		int year = Integer.parseInt(request.getParameter("sYear"));
		SalaryModel model = getUserBroker().getSalaryDetails(user, month, year);
		double advances = getUserBroker().getTotalAdvance(user,month, year);
		model.setAdvances(advances);
		model.setUser(user);
		request.setAttribute(FORM_DATA, model);
		EntryModel[] entries = getUserBroker().getAdvanceEntries(user,month,year); 
		request.setAttribute(CURRENT_ADVANCES, entries);
		return initializeSalaryRpt(request);
	}

	private String initializeSalaryRpt(HttpServletRequest request) {
		// TODO Auto-generated method stub
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users); 
		return "salaryRpt.jsp";
	}

	private String getSalaryDetails(HttpServletRequest request) {
		String pageType ="salary.jsp";
		UserModel user = (UserModel) request.getSession().getAttribute(USER);
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		SalaryModel previous = getUserBroker().getSalaryDetails(user, month-1, year);
		request.setAttribute(PREVIOUS_SALARY, previous);
		SalaryModel model = getUserBroker().getSalaryDetails(user, month, year);
		double advances = getUserBroker().getTotalAdvance(user,month, year);
		model.setAdvances(advances);
		request.setAttribute(FORM_DATA, model);
		EntryModel[] entries = getUserBroker().getAdvanceEntries(user,month,year); 
		request.setAttribute(CURRENT_ADVANCES, entries);
		TargetModel target = getUserBroker().getTargetDetails(user);
		request.setAttribute(CURRENT_TARGET, target);
		return pageType;
	}

}

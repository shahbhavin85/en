package com.en.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.SalesmanDailyBroker;
import com.en.broker.UserBroker;
import com.en.model.AppointmentModel;
import com.en.model.AttendanceModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesmanDlyItmModel;
import com.en.model.SalesmanDlyRptModel;
import com.en.model.UserModel;
import com.en.util.StringUtility;
import com.en.util.Utils;

public class SalesmanDailyHandler extends Basehandler {

	public static String getPageName() {
		return SALESMAN_DLY_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "salDlyRpt1.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(INIT_SALESMAN_DLY)){
			//addItem(request,response);			
		} else if(strAction.equals(INIT_SALESMAN_DLY1)){
			pageType = getSalesmanDtls(request);			
		} else if(strAction.equals(ADD_SALESMAN_ITM)){
			pageType = "salDlyRpt2.jsp";
			addReportItem(request);			
		} else if(strAction.equals(DEL_SALESMAN_ITM)){
			pageType = "salDlyRpt2.jsp";
			delReportItem(request);			
		} else if(strAction.equals(SUB_SALESMAN_APP)){
			pageType = "salDlyRpt1.jsp";
			submitReport(request);			
		} else if(strAction.equals(VIEW_SALESMAN_RPT)){
			pageType = "viewDailyRpt.jsp";
			initializeViewReport(request);			
		} else if(strAction.equals(GET_SALESMAN_RPT)){
			pageType = "viewDailyRpt.jsp";
			viewReport(request);			
		} else if(strAction.equals(PRINT_SALESMAN_RPT)){
			pageType = "printDailyRpt.jsp";
			viewReport(request);			
		} else if(strAction.equals(APPROVAL_SALESMAN_RPT)){
			pageType = "approvalDailyRpt.jsp";
			approvalPendingReport(request);			
		} else if(strAction.equals(APPROVAL_SALESMAN_RPT_DTLS)){
			pageType = "salesmanApprovalRpt.jsp";
			viewReport(request);			
		} else if(strAction.equals(REJECT_SALESMAN_RPT)){
			pageType = "approvalDailyRpt.jsp";
			rejectReport(request);			
		} else if(strAction.equals(APPROVE_SALESMAN_RPT)){
			pageType = "approvalDailyRpt.jsp";
			approveReport(request);			
		} else if(strAction.equals(INIT_SALESMAN_STATUS_RPT)){
			pageType = "salesmanStatusRpt.jsp";
			initializeStatusRpt(request);			
		} else if(strAction.equals(GET_SALESMAN_STATUS_RPT)){
			pageType = "salesmanStatusRpt.jsp";
			getStatusRpt(request);			
		} else if(strAction.equals(PRINT_SALESMAN_STATUS_RPT)){
			pageType = "printSalesmanStatusRpt.jsp";
			getStatusRpt(request);			
		}
		return pageType;
	}

	private void getStatusRpt(HttpServletRequest request) {
		initializeStatusRpt(request);
		String userId = request.getParameter("txtUserId");
//		if(request.getSession().getAttribute(ACCESS_POINT).equals("1")){
//			userId = ((UserModel) request.getSession().getAttribute(USER)).getUserId();
//		} 
		String fromDate = request.getParameter("txtFromDate");
		String toDate = request.getParameter("txtToDate");
		String status = request.getParameter("sStatus");
		ArrayList<String[]> rptDtls = getSalesmanDailyBroker().getStatusReport(userId, fromDate, toDate, status);
		request.setAttribute("reportList", rptDtls);
		request.setAttribute(USER, userId);
		request.setAttribute("status", status);
		request.setAttribute("fromDate", fromDate);
		request.setAttribute("toDate", toDate);
	}

	private void initializeStatusRpt(HttpServletRequest request) {
		UserModel[] users = getUsers((UserModel) request.getSession().getAttribute(USER), getUserBroker());
		request.setAttribute(USERS, users);
		return;
	}
	
	private UserModel[] getUsers(UserModel user, UserBroker broker){
		ArrayList<UserModel> lst = new ArrayList<UserModel>();
		UserModel[] users = broker.getManagerUsers(user);
		for(int i=0; i<users.length; i++){
//			lst.add(users[i]);
			UserModel[] temp = getUsers(users[i], broker);
			for(int k=0; k<temp.length; k++){
				lst.add(temp[k]);
			}
		}
		lst.add(user);
		return (UserModel[]) lst.toArray(new UserModel[0]);
	}

	private void approveReport(HttpServletRequest request) {
		SalesmanDailyBroker broker = getSalesmanDailyBroker();
		String rptId = request.getParameter("txtRptId");
		String attDate = request.getParameter("txtRptDate");
		AttendanceModel attendance = new AttendanceModel();
		attendance.getUser().setUserId(request.getParameter("txtUserId"));
		attendance.setType(request.getParameter("att"));
		attendance.setInTime(request.getParameter("inTime"));
		attendance.setOutTime(request.getParameter("outTime"));
		attendance.setLateFine(request.getParameter("lateFine"));
		attendance.setPanelty(request.getParameter("panelty"));
		attendance.setRemarks(request.getParameter("remark"));
		attendance.setOt("0");
		attendance.getUser().setType(Integer.parseInt(request.getParameter("txtUserType")));
		MessageModel msg = broker.approveReport(rptId,(UserModel) request.getSession().getAttribute(USER));
		MessageModel msg1 = getAttendanceBroker().updateUserAttendance(attDate, attendance);
		msg.addNewMessage(msg1.getMessages().get(0));
		request.setAttribute(MESSAGES, msg);
		SalesmanDlyRptModel[] items = getSalesmanDailyBroker().getPendingApprovalReport((UserModel) request.getSession().getAttribute(USER));
		request.setAttribute(FORM_DATA, items);
	}

	private void rejectReport(HttpServletRequest request) {
		SalesmanDailyBroker broker = getSalesmanDailyBroker();
		String rptId = request.getParameter("txtRptId");
		String remark = request.getParameter("taRemark");
		MessageModel msg = broker.rejectReport(rptId, remark);
		request.setAttribute(MESSAGES, msg);
		SalesmanDlyRptModel[] items = getSalesmanDailyBroker().getPendingApprovalReport((UserModel) request.getSession().getAttribute(USER));
		request.setAttribute(FORM_DATA, items);
	}

	private void approvalPendingReport(HttpServletRequest request) {
		SalesmanDlyRptModel[] items = getSalesmanDailyBroker().getPendingApprovalReport((UserModel) request.getSession().getAttribute(USER));
		request.setAttribute(FORM_DATA, items);
		return;
	}

	private void initializeViewReport(HttpServletRequest request) {
		UserModel[] users = getUsers((UserModel) request.getSession().getAttribute(USER), getUserBroker());
		request.setAttribute(USERS, users);
		return;
	}

	private void viewReport(HttpServletRequest request) {
		SalesmanDailyBroker broker = getSalesmanDailyBroker();
		String rptDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String userId = "";
		UserModel[] users = getUsers((UserModel) request.getSession().getAttribute(USER), getUserBroker());
		request.setAttribute(USERS, users);
		userId = request.getParameter("txtUserId");
		String status = broker.checkStatus(userId, rptDt);
		if(status!=null && !status.equals("Approved") && !status.equals("Awaiting Approval")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Report not submitted."));
			request.setAttribute(MESSAGES, msg);
			return;
		}
		SalesmanDlyRptModel model = broker.getDailyReport(userId, rptDt);
		request.setAttribute(FORM_DATA, model);
		if(model == null){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Report for that date doesn't exist."));
			request.setAttribute(MESSAGES, msg);
		}
		return;
	}

	private void submitReport(HttpServletRequest request) {
		SalesmanDailyBroker broker = getSalesmanDailyBroker();
		String rptDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String userId = "";
		if(request.getSession().getAttribute(ACCESS_POINT).equals("1")){
			userId = ((UserModel) request.getSession().getAttribute(USER)).getUserId();
		} else if(request.getSession().getAttribute(ACCESS_POINT).equals("0")){
			userId = request.getParameter("txtUserId");
		}
		String rptId = request.getParameter("txtRptId");
		String status = broker.checkStatus(userId, rptDt);
		if(status!=null && status.equals("Approved")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Already approved. Please contact the admin."));
			request.setAttribute(MESSAGES, msg);
		} else if(status!=null && status.equals("Awaiting Approval")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Already submitted for approval."));
			request.setAttribute(MESSAGES, msg);
		} else {
			MessageModel msg = broker.submitApproval(rptId);
			request.setAttribute(MESSAGES, msg);
		}
	}

	private void delReportItem(HttpServletRequest request) {
		SalesmanDailyBroker broker = getSalesmanDailyBroker();
		String rptDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String userId = "";
		if(request.getSession().getAttribute(ACCESS_POINT).equals("1")){
			userId = ((UserModel) request.getSession().getAttribute(USER)).getUserId();
		} else if(request.getSession().getAttribute(ACCESS_POINT).equals("0")){
			userId = request.getParameter("txtUserId");
		}
		String rptId = request.getParameter("txtRptId");
		String index = request.getParameter("txtIndex");
		String status = broker.checkStatus(userId, rptDt);
		if(status!=null && status.equals("Approved")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Already approved. Please contact the admin."));
			request.setAttribute(MESSAGES, msg);
		} else if(status!=null && status.equals("Awaiting Approval")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Already submitted for approval."));
			request.setAttribute(MESSAGES, msg);
		} else {
			MessageModel msg = broker.delItm(index, rptId);
			request.setAttribute(MESSAGES, msg);
			SalesmanDlyRptModel model = broker.getDailyReport(userId, rptDt);
			if(model == null){
				model = new SalesmanDlyRptModel();
				model.setRptDt(rptDt);
				model.getUser().setUserId(userId);
				model.setStatus(status);
			}
//			CustomerModel[] customers = getCustomerBroker().getCustomer1();
//			request.setAttribute(CUSTOMERS, customers);
			request.setAttribute(FORM_DATA, model);
		}
	}

	private String getSalesmanDtls(HttpServletRequest request) {
		String pageType = "salDlyRpt2.jsp";
		SalesmanDailyBroker broker = getSalesmanDailyBroker();
		String rptDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String userId = "";
		DateFormat formatter ; 
		Date date = new Date(); 
		Date currDate = new Date();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			date = (Date)formatter.parse(rptDt);
		} catch (Exception e){
			e.printStackTrace();
		}
//		if(request.getSession().getAttribute(ACCESS_POINT).equals("1")){
//			userId = ((UserModel) request.getSession().getAttribute(USER)).getUserId();
//		} else if(request.getSession().getAttribute(ACCESS_POINT).equals("0")){
			userId = request.getParameter("txtUserId");
//		}
		String status = broker.checkStatus(userId, rptDt);
		if(currDate.getTime() < date.getTime()){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Report date should be on or before current date."));
			request.setAttribute(MESSAGES, msg);
			pageType = "salDlyRpt1.jsp";
		} else if(getSalesmanEnquiryBroker().isPendingAppointment(rptDt, userId)){
			AppointmentModel[] actions = getSalesmanEnquiryBroker().getPendingAppointment(rptDt,userId);
			request.setAttribute(PENDING_APP_LIST, actions);
			pageType = "salesmanPendingApp.jsp";
		} else if(status!=null && status.equals("Approved")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Already approved. Please contact the admin."));
			request.setAttribute(MESSAGES, msg);
			pageType = "salDlyRpt1.jsp";
		} else if(status!=null && status.equals("Awaiting Approval")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Already submitted for approval."));
			request.setAttribute(MESSAGES, msg);
			pageType = "salDlyRpt1.jsp";
		} else {
			SalesmanDlyRptModel model = broker.getDailyReport(userId, rptDt);
			if(model == null){
				model = new SalesmanDlyRptModel();
				model.setRptDt(rptDt);
				model.getUser().setUserId(userId);
				model.setStatus(status);
			}
//			CustomerModel[] customers = getCustomerBroker().getCustomer1();
//			request.setAttribute(CUSTOMERS, customers);
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private void addReportItem(HttpServletRequest request){
		SalesmanDailyBroker broker = getSalesmanDailyBroker();
		String rptDt = StringUtility.convertToSQLAddress(request.getParameter("txtRptDate"));
		String userId = "";
		if(request.getSession().getAttribute(ACCESS_POINT).equals("1")){
			userId = ((UserModel) request.getSession().getAttribute(USER)).getUserId();
		} else if(request.getSession().getAttribute(ACCESS_POINT).equals("0")){
			userId = StringUtility.convertToSQLAddress(request.getParameter("txtUserId"));
		}
		String status = broker.checkStatus(userId, rptDt);
		if(status!=null && status.equals("Approved")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Already approved. Please contact the admin."));
			request.setAttribute(MESSAGES, msg);
		} else {
			SalesmanDlyItmModel item = populateItemModel(request);
			MessageModel msg = broker.addItm(item, userId, rptDt);
			request.setAttribute(MESSAGES, msg);
			SalesmanDlyRptModel model = broker.getDailyReport(userId, rptDt);
			if(model == null){
				model = new SalesmanDlyRptModel();
				model.setRptDt(rptDt);
				model.getUser().setUserId(userId);
				model.setStatus(status);
			}
//			CustomerModel[] customers = getCustomerBroker().getCustomer1();
//			request.setAttribute(CUSTOMERS, customers);
			request.setAttribute(FORM_DATA, model);
		}
	}

	private SalesmanDlyItmModel populateItemModel(HttpServletRequest request) {
		SalesmanDlyItmModel model = new SalesmanDlyItmModel();
		if(request.getParameter("txtInTime") != null && !request.getParameter("txtInTime").trim().equals("")){
			String inTime = "";
			if(request.getParameter("txtInTime").substring(request.getParameter("txtInTime").length()-2).equals("am")){
				if(request.getParameter("txtInTime").substring(0,request.getParameter("txtInTime").length()-2).length() == 5){
					inTime = "0"+request.getParameter("txtInTime").substring(0,request.getParameter("txtInTime").length()-3)+":00";
				} else if(request.getParameter("txtInTime").substring(0,2).equals("12")){
					inTime = "00:00:00";
				} else {
					inTime = request.getParameter("txtInTime").substring(0,request.getParameter("txtInTime").length()-3)+":00";
				}
			} else {
				if(request.getParameter("txtInTime").substring(0,2).equals("12")){
					inTime = "12"+request.getParameter("txtInTime").substring(2,request.getParameter("txtInTime").length()-3)+":00";
				} else {
					inTime = (Integer.parseInt(request.getParameter("txtInTime").substring(0,(request.getParameter("txtInTime").indexOf(':'))))+12)+request.getParameter("txtInTime").substring(request.getParameter("txtInTime").indexOf(':'),request.getParameter("txtInTime").length()-3)+":00";
				}
			}
			
			model.setInTime(inTime);
		}
		if(request.getParameter("txtOutTime") != null && !request.getParameter("txtOutTime").trim().equals("")){
			String outTime = "";
			if(request.getParameter("txtOutTime").substring(request.getParameter("txtOutTime").length()-2).equals("am")){
				if(request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-2).length() == 5){
					outTime = "0"+request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-3)+":00";
				} else if(request.getParameter("txtOutTime").substring(0,2).equals("12")){
					outTime = "00:00:00";
				} else {
					outTime = request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-3)+":00";
				}
			} else {
				if(request.getParameter("txtOutTime").substring(0,2).equals("12")){
					outTime = "12"+request.getParameter("txtOutTime").substring(2,request.getParameter("txtOutTime").length()-3)+":00";
				} else {
					outTime = (Integer.parseInt(request.getParameter("txtOutTime").substring(0,(request.getParameter("txtOutTime").indexOf(':'))))+12)+request.getParameter("txtOutTime").substring(request.getParameter("txtOutTime").indexOf(':'),request.getParameter("txtOutTime").length()-3)+":00";
				}
			}
			
			model.setOutTime(outTime);			
		}
		if(request.getParameter("rType") != null && !request.getParameter("rType").trim().equals(""))
			model.setType(request.getParameter("rType"));
		if(request.getParameter("taRemark") != null && !request.getParameter("taRemark").trim().equals(""))
			model.setRemark(request.getParameter("taRemark"));
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals("--"))
			model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("txtLodging") != null && !request.getParameter("txtLodging").trim().equals(""))
			model.setLodging(Double.parseDouble(request.getParameter("txtLodging")));
		if(request.getParameter("txtFood") != null && !request.getParameter("txtFood").trim().equals(""))
			model.setFood(Double.parseDouble(request.getParameter("txtFood")));
		if(request.getParameter("txtLocalTransport") != null && !request.getParameter("txtLocalTransport").trim().equals(""))
			model.setLocaltransport(Double.parseDouble(request.getParameter("txtLocalTransport")));
		if(request.getParameter("txtRailBus") != null && !request.getParameter("txtRailBus").trim().equals(""))
			model.setRailbus(Double.parseDouble(request.getParameter("txtRailBus")));
		if(request.getParameter("txtCourier") != null && !request.getParameter("txtCourier").trim().equals(""))
			model.setCourier(Double.parseDouble(request.getParameter("txtCourier")));
		if(request.getParameter("txtOthers") != null && !request.getParameter("txtOthers").trim().equals(""))
			model.setOthers(Double.parseDouble(request.getParameter("txtOthers")));
		return model;
	}

}

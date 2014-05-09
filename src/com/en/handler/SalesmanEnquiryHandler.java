package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.ActionModel;
import com.en.model.AppointmentModel;
import com.en.model.EnquiryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesmanDlyItmModel;
import com.en.model.UserModel;
import com.en.util.ActionType;
import com.en.util.Utils;

public class SalesmanEnquiryHandler extends Basehandler {

	public static String getPageName() {
		return SALESMAN_ENQUIRY_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pagetype = "salesmanInitEnquiry.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
		if(strAction.equals(GET_PENDING_APPOINTMENT)){
			pagetype = getPendingAppointments(request);
		} else if(strAction.equals(GET_ENQUIRY)){
			getEnquiry(request);
			pagetype = "getEnquiryPopup.jsp";
		} else if(strAction.equals(INIT_REPLY_PENDING_APPOINTMENT)){
			initailizeReply(request);
			pagetype = "salesmanAppReply.jsp";
		} else if(strAction.equals(INIT_DELAY_PENDING_APPOINTMENT)){
			initailizeDelay(request);
			pagetype = "salesmanAppReply.jsp";
		} else if(strAction.equals(INIT_CANCEL_PENDING_APPOINTMENT)){
			initailizeCancel(request);
			pagetype = "salesmanAppReply.jsp";
		}  else if(strAction.equals(REPLY_PENDING_APPOINTMENT)){
			confirmReply(request);
			pagetype =  getPendingAppointments(request);
		} else if(strAction.equals(DELAY_PENDING_APPOINTMENT)){
			confirmDelay(request);
			pagetype =  getPendingAppointments(request);
		} else if(strAction.equals(CANCEL_PENDING_APPOINTMENT)){
			confirmCancel(request);
			pagetype =  getPendingAppointments(request);
		}
		return pagetype;
	}

	private void confirmCancel(HttpServletRequest request) {
		ActionModel action = new ActionModel();
		action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
		int actionType = Integer.parseInt(request.getParameter("sType"));
		action.setActionType(actionType+"");
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		action.setRemarks(request.getParameter("taRemark"));
		getEnquiryBroker().addAction(action, enqNo);
		if(request.getParameter("rAction").equals("alert")){
			action = new ActionModel();
			action.setAppDt(request.getParameter("txtAlertDate"));
			action.setRemarks(request.getParameter("taAlertRemark"));
			action.setActionType(ActionType.ENQUIRY_ALERT);
			action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
			getEnquiryBroker().addAction(action, enqNo);
		} else if(request.getParameter("rAction").equals("reApp")){
			action = new ActionModel();
			action.getSalesman().setUserId(((UserModel)request.getSession().getAttribute(USER)).getUserId());
			action.setAppDt(request.getParameter("txtReAppointDate"));
			if(request.getParameter("txtReAppointTime") != null && !request.getParameter("txtReAppointTime").trim().equals("")){
				String appTime = "";
				if(request.getParameter("txtReAppointTime").substring(request.getParameter("txtReAppointTime").length()-2).equals("am")){
					if(request.getParameter("txtReAppointTime").substring(0,request.getParameter("txtReAppointTime").length()-2).length() == 5){
						appTime = "0"+request.getParameter("txtReAppointTime").substring(0,request.getParameter("txtReAppointTime").length()-3)+":00";
					} else if(request.getParameter("txtReAppointTime").substring(0,2).equals("12")){
						appTime = "00:00:00";
					} else {
						appTime = request.getParameter("txtReAppointTime").substring(0,request.getParameter("txtReAppointTime").length()-3)+":00";
					}
				} else {
					if(request.getParameter("txtReAppointTime").substring(0,2).equals("12")){
						appTime = "12"+request.getParameter("txtReAppointTime").substring(2,request.getParameter("txtReAppointTime").length()-3)+":00";
					} else {
						appTime = (Integer.parseInt(request.getParameter("txtReAppointTime").substring(0,(request.getParameter("txtReAppointTime").indexOf(':'))))+12)+request.getParameter("txtReAppointTime").substring(request.getParameter("txtReAppointTime").indexOf(':'),request.getParameter("txtReAppointTime").length()-3)+":00";
					}
				}
				
				action.setAppTm(appTime);
				action.setRemarks(request.getParameter("taReRemark"));
			}
			action.setActionType(ActionType.APPOINTMENT);
			action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
			getEnquiryBroker().addAction(action, enqNo);
		} else if(request.getParameter("rAction").equals("close")){
			action.setRemarks(request.getParameter("taCloseRemark"));
			action.setActionType(ActionType.CLOSED_APPROVAL);
			action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
			getEnquiryBroker().addAction(action, enqNo);
		}
	}

	private void confirmDelay(HttpServletRequest request) {
		ActionModel action = new ActionModel();
		String userId = ((UserModel)request.getSession().getAttribute(USER)).getUserId();
		action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
		action.getSalesman().setUserId(userId);
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		action.setAppDt(request.getParameter("txtAppointDate"));
		if(request.getParameter("txtAppointTime") != null && !request.getParameter("txtAppointTime").trim().equals("")){
			String appTime = "";
			if(request.getParameter("txtAppointTime").substring(request.getParameter("txtAppointTime").length()-2).equals("am")){
				if(request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-2).length() == 5){
					appTime = "0"+request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-3)+":00";
				} else if(request.getParameter("txtAppointTime").substring(0,2).equals("12")){
					appTime = "00:00:00";
				} else {
					appTime = request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-3)+":00";
				}
			} else {
				if(request.getParameter("txtAppointTime").substring(0,2).equals("12")){
					appTime = "12"+request.getParameter("txtAppointTime").substring(2,request.getParameter("txtAppointTime").length()-3)+":00";
				} else {
					appTime = (Integer.parseInt(request.getParameter("txtAppointTime").substring(0,(request.getParameter("txtAppointTime").indexOf(':'))))+12)+request.getParameter("txtAppointTime").substring(request.getParameter("txtAppointTime").indexOf(':'),request.getParameter("txtAppointTime").length()-3)+":00";
				}
			}
			
			action.setAppTm(appTime);
		}
		action.setRemarks(request.getParameter("taRemark"));
		getEnquiryBroker().addAction(action, enqNo);
	}

	private void confirmReply(HttpServletRequest request) {
		ActionModel action = new ActionModel();
		String userId = ((UserModel)request.getSession().getAttribute(USER)).getUserId();
		action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
		int actionType = Integer.parseInt(request.getParameter("sType"));
		action.setActionType(actionType+"");
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		action.setAppDt(request.getParameter("txtAppointDate"));
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
			action.setInTm(inTime);
		}
		if(request.getParameter("txtOutTime") != null && !request.getParameter("txtOutTime").trim().equals("")){
			String inTime = "";
			if(request.getParameter("txtOutTime").substring(request.getParameter("txtOutTime").length()-2).equals("am")){
				if(request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-2).length() == 5){
					inTime = "0"+request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-3)+":00";
				} else if(request.getParameter("txtOutTime").substring(0,2).equals("12")){
					inTime = "00:00:00";
				} else {
					inTime = request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-3)+":00";
				}
			} else {
				if(request.getParameter("txtOutTime").substring(0,2).equals("12")){
					inTime = "12"+request.getParameter("txtOutTime").substring(2,request.getParameter("txtOutTime").length()-3)+":00";
				} else {
					inTime = (Integer.parseInt(request.getParameter("txtOutTime").substring(0,(request.getParameter("txtOutTime").indexOf(':'))))+12)+request.getParameter("txtOutTime").substring(request.getParameter("txtOutTime").indexOf(':'),request.getParameter("txtOutTime").length()-3)+":00";
				}
			}
			action.setOutTm(inTime);
		}
		action.setRemarks(request.getParameter("taRemark"));
		getEnquiryBroker().addAction(action, enqNo);
		int customerId = getEnquiryBroker().getEnquiryDetails(enqNo).getCustomer().getCustomerId();
		SalesmanDlyItmModel model = populateItemModel(action, customerId);
		getSalesmanDailyBroker().addItm(model, userId, action.getAppDt());
		if(request.getParameter("rAction").equals("alert")){
			action = new ActionModel();
			action.setAppDt(request.getParameter("txtAlertDate"));
			action.setRemarks(request.getParameter("taAlertRemark"));
			action.setActionType(ActionType.ENQUIRY_ALERT);
			action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
			getEnquiryBroker().addAction(action, enqNo);
		} else if(request.getParameter("rAction").equals("reApp")){
			action = new ActionModel();
			action.getSalesman().setUserId(((UserModel)request.getSession().getAttribute(USER)).getUserId());
			action.setAppDt(request.getParameter("txtReAppointDate"));
			if(request.getParameter("txtReAppointTime") != null && !request.getParameter("txtReAppointTime").trim().equals("")){
				String appTime = "";
				if(request.getParameter("txtReAppointTime").substring(request.getParameter("txtReAppointTime").length()-2).equals("am")){
					if(request.getParameter("txtReAppointTime").substring(0,request.getParameter("txtReAppointTime").length()-2).length() == 5){
						appTime = "0"+request.getParameter("txtReAppointTime").substring(0,request.getParameter("txtReAppointTime").length()-3)+":00";
					} else if(request.getParameter("txtReAppointTime").substring(0,2).equals("12")){
						appTime = "00:00:00";
					} else {
						appTime = request.getParameter("txtReAppointTime").substring(0,request.getParameter("txtReAppointTime").length()-3)+":00";
					}
				} else {
					if(request.getParameter("txtReAppointTime").substring(0,2).equals("12")){
						appTime = "12"+request.getParameter("txtReAppointTime").substring(2,request.getParameter("txtReAppointTime").length()-3)+":00";
					} else {
						appTime = (Integer.parseInt(request.getParameter("txtReAppointTime").substring(0,(request.getParameter("txtReAppointTime").indexOf(':'))))+12)+request.getParameter("txtReAppointTime").substring(request.getParameter("txtReAppointTime").indexOf(':'),request.getParameter("txtReAppointTime").length()-3)+":00";
					}
				}
				
				action.setAppTm(appTime);
				action.setRemarks(request.getParameter("taReRemark"));
			}
			action.setActionType(ActionType.APPOINTMENT);
			action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
			getEnquiryBroker().addAction(action, enqNo);
		} else if(request.getParameter("rAction").equals("close")){
			action.setRemarks(request.getParameter("taCloseRemark"));
			action.setActionType(ActionType.CLOSED_APPROVAL);
			action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
			getEnquiryBroker().addAction(action, enqNo);
		}
	}

	private void initailizeCancel(HttpServletRequest request) {
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		request.setAttribute("EnqNo", enqNo);
		String appDt = Utils.convertToAppDate(request.getParameter("txtAppDt"));
		request.setAttribute("AppDt", appDt);
		request.setAttribute("sType", Integer.parseInt(ActionType.APPOINTMENT_CANCEL));
	}

	private void initailizeDelay(HttpServletRequest request) {
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		request.setAttribute("EnqNo", enqNo);
		String appDt = Utils.convertToAppDate(request.getParameter("txtAppDt"));
		request.setAttribute("AppDt", appDt);
		request.setAttribute("sType", Integer.parseInt(ActionType.APPOINTMENT_DELAY));
	}

	private void initailizeReply(HttpServletRequest request) {
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		request.setAttribute("EnqNo", enqNo);
		String appDt = Utils.convertToAppDate(request.getParameter("txtAppDt"));
		request.setAttribute("AppDt", appDt);
		request.setAttribute("sType", Integer.parseInt(ActionType.APPOINTMENT_REPLY));
	}

	private void getEnquiry(HttpServletRequest request) {
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		EnquiryModel model = getEnquiryBroker().getEnquiryDetails(enqNo);
		request.setAttribute(FORM_DATA, model);
		return;
	}

	private String getPendingAppointments(HttpServletRequest request) {
		String returnString = "salesmanInitEnquiry.jsp";
		String date = request.getParameter("txtRptDate");
		String userId = ((UserModel) request.getSession().getAttribute(USER)).getUserId();
		AppointmentModel[] actions = getSalesmanEnquiryBroker().getPendingAppointment(Utils.convertToSQLDate(date),userId);
		if(actions == null || actions.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "No Appointments for the selected date."));
			request.setAttribute(MESSAGES, msg);
			returnString = "salesmanInitEnquiry.jsp";
		} else {
			request.setAttribute(PENDING_APP_LIST, actions);
			returnString = "salesmanPendingApp.jsp";
		}
		return returnString;
	}

	private SalesmanDlyItmModel populateItemModel(ActionModel actionModel, int customerId) {
		SalesmanDlyItmModel model = new SalesmanDlyItmModel();
		model.setInTime(actionModel.getInTm());
		model.setOutTime(actionModel.getOutTm());			
		model.setType("0");
		model.setRemark(actionModel.getRemarks());
		model.getCustomer().setCustomerId(customerId);
		model.setEditable("N");
		return model;
	}

}

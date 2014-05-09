package com.en.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.EntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesItemModel;
import com.en.model.SalesModel;
import com.en.model.UserModel;
import com.en.util.Constant;
import com.en.util.DateUtil;
import com.en.util.EmailUtil;
import com.en.util.Utils;

public class CollectionHandler extends Basehandler {

	public static String getPageName() {
		return COLLECTION_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "initOutstandingRpt.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
		if(strAction.equals(GET_OUTSTANDING_RPT)){
			pageType = getOutstandingReport(request);
		} else if(strAction.equals(GET_BILL_PAYMENT_DTLS)){
			pageType = getBillPaymentDtls(request);
		} else if(strAction.equals(GET_CUST_OUTSTANDING_RPT)){
			pageType = getCustomerOutstandingReport(request);
		} else if(strAction.equals(ADD_RMK_CUST_OUTSTANDING_RPT)){
			pageType = getAddRemark(request);
		} else if(strAction.equals(GET_FOLLOWUP_DTLS)){
			pageType = getFollowupDtls(request);
		} else if(strAction.equals(BACK_OUTSTANDING_RPT)){
			pageType = getBackOutstandingRpt(request);
		} else if(strAction.equals(VIEW_CUST_OUTSTANDING_RPT)){
			getCustomerOutstandingReport(request);
			request.setAttribute("cBranch", request.getParameterValues("cBranch"));
			pageType = "viewAlertCollection.jsp";
		} else if(strAction.equals(GET_SALES)){
			pageType = getSalesDtls(request);
		} else {
			initialize(request);
		}
		return pageType;
	}

	private String getFollowupDtls(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		request.setAttribute("billNo", billNo);
		SalesModel[] model = getCollectionBroker().getFollowupDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return "viewFollowupDtls.jsp";
	}

	private String getAddRemark(HttpServletRequest request) {
		String[] billNos = request.getParameterValues("chkBill");
		String followupDt = request.getParameter("txtFollowDate");
		String followupRemark = request.getParameter("txtRemarks");
		getCollectionBroker().addFollowupRemarks(billNos, followupDt, followupRemark, ((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId());
		return getBackOutstandingRpt(request);
	}

	private String getBackOutstandingRpt(HttpServletRequest request) {
		String pageType = "outstandingRpt.jsp";
		String branch = request.getParameter("branches");
		ArrayList<String> branches = new ArrayList<String>(0);
		StringTokenizer stRow = new StringTokenizer(branch,"|");
		while (stRow.hasMoreElements()) {
			branches.add(stRow.nextToken());
		}
		request.setAttribute("cBranch", (String[]) branches.toArray(new String[0]));
		String userId = request.getParameter("sUser");
		request.setAttribute(USER, userId);
		SalesModel[] outstandingBills = getCollectionBroker().getOutstandingRpt("",(String[]) branches.toArray(new String[0]), userId);  //new SalesModel[0];
		if(outstandingBills.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "initOutstandingRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, outstandingBills);
			HashMap<Integer, Double> totalOutstanding = new HashMap<Integer, Double>(0);
			SalesModel temp = null;
			double roundOff = 0;
			for(int i=0; i<outstandingBills.length; i++){
				temp = outstandingBills[i];
				roundOff = (1-Utils.get2DecimalDouble((temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())%1)) ;
				if(totalOutstanding.get(temp.getCustomer().getCustomerId()) != null){
					totalOutstanding.put(temp.getCustomer().getCustomerId(), totalOutstanding.get(temp.getCustomer().getCustomerId())+(Utils.get2DecimalDouble(Utils.get2DecimalDouble(temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())+roundOff-temp.getPayAmt())));
				} else {
					totalOutstanding.put(temp.getCustomer().getCustomerId(), (Utils.get2DecimalDouble(Utils.get2DecimalDouble(temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())+roundOff-temp.getPayAmt())));
				}
			}
			request.setAttribute("totalOutstandingList", totalOutstanding);
		}
		initialize(request);
		return pageType;
	}

	private String getCustomerOutstandingReport(HttpServletRequest request) {
		String pageType = "viewOutstandingDtls.jsp";
		int customerId = Integer.parseInt(request.getParameter("custId"));
		String branch = request.getParameter("branches");
		ArrayList<String> branches = new ArrayList<String>(0);
		StringTokenizer stRow = new StringTokenizer(branch,"|");
		while (stRow.hasMoreElements()) {
			branches.add(stRow.nextToken());
		}
		String userId = request.getParameter("sUser");
		request.setAttribute(USER, userId);
		SalesModel[] outstandingBills = getCollectionBroker().getOutstandingRpt(customerId+"",(String[]) branches.toArray(new String[0]), userId);  //new SalesModel[0];
		request.setAttribute(FORM_DATA, outstandingBills);
		CustomerModel customer = getCustomerBroker().getCustomerDtls(customerId);
		request.setAttribute("customer", customer);
		String email = request.getParameter("email");
		if(email != null && email.equals("Y")){
			AccessPointModel[] access = getAccessPointBroker().getAccessPoint();
			request.setAttribute(ACCESS_POINTS, branches);
			MessageModel msg = EmailUtil.sendOutstandingLstMail(outstandingBills, customer, (String[]) branches.toArray(new String[0]),access);
			request.setAttribute(MESSAGES, msg);
		}
		String sms = request.getParameter("sms");
		if(sms != null && sms.equals("Y")){
			MessageModel msg = new MessageModel();
//			AccessPointModel[] access = getAccessPointBroker().getAccessPoint();
//			request.setAttribute(ACCESS_POINTS, branches);
			try{
				double total = 0;
				for(int i=0; i<outstandingBills.length; i++){
					total = total + (outstandingBills[i].getTotalAmt()+outstandingBills[i].getInstallation()+outstandingBills[i].getPacking()+outstandingBills[i].getForwarding()-outstandingBills[i].getLess()-outstandingBills[i].getPayAmt());
				}
				String Message = URLEncoder.encode("Your Total Outstanding as on Dt "+DateUtil.getCurrDt()+" is Rs.  "+Utils.get2Decimal(total)+". kindly arrange to send payment. Regards HESHST","UTF-8");
//				String inputLine;
				URL url;
				BufferedReader bf;
				if(!customer.getMobile1().equals("")){
					url = new URL(AUTH_SMS_BASE_URL+"&To="+customer.getMobile1()+"&Message="+Message);
					bf = new BufferedReader(new InputStreamReader(url.openStream()));
					/*while((inputLine = bf.readLine()) != null)
					System.out.println(inputLine);
				System.out.println(inputLine);*/
					bf.close();
				}
				if(!customer.getMobile2().equals("")){
					url = new URL(AUTH_SMS_BASE_URL+"&To="+customer.getMobile2()+"&Message="+Message);
					bf = new BufferedReader(new InputStreamReader(url.openStream()));
					/*while((inputLine = bf.readLine()) != null)
					System.out.println(inputLine);
				System.out.println(inputLine);*/
					bf.close();
				}
				msg.addNewMessage(new Message(SUCCESS, "Message Sent Successfully!!"));
			} catch (Exception e) {
				msg.addNewMessage(new Message(ERROR, e.getMessage()));
			}
		}
		EntryModel[] lastTxns = getBranchDayEntryBroker().getLastTransactions(customerId);
		request.setAttribute(CUST_LAST_TXN, lastTxns);
		return pageType;
	}

	private String getBillPaymentDtls(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		request.setAttribute("billNo", billNo);
		EntryModel[] model = getCollectionBroker().getPaymentDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		EntryModel[] adjustEntries = getCollectionBroker().getAdjustDetails(billNo);
		request.setAttribute(ADJUST_ENTRIES, adjustEntries);
		return "viewPaymentDtls.jsp";
	}

	private String getSalesDtls(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		String email = request.getParameter("email");
		if(email != null && email.equals("Y")){
			MessageModel msg = EmailUtil.sendSalesMail(model);
			request.setAttribute(MESSAGES, msg);
		}
		String sms = request.getParameter("sms");
		if(sms != null && sms.equals("Y")){
			MessageModel msg = sendSalesDtlsSMS(model);
			request.setAttribute(MESSAGES, msg);
		}
		request.setAttribute(FORM_DATA, model);
		return "viewSalesDtls.jsp";
	}

	
	private MessageModel sendSalesDtlsSMS(SalesModel order){
		MessageModel msg = new MessageModel();
		try{
			ArrayList<SalesItemModel> items = order.getItems();
			Iterator<SalesItemModel> itrItem = items.iterator();
			SalesItemModel item = null;
			double qty = 0;
			double total = 0;
			while(itrItem.hasNext()){
				item = itrItem.next();
				qty = qty + item.getQty();
				total = total + ((item.getQty()*Double.parseDouble(item.getItem().getItemPrice()))*((100-item.getDisrate())/100)*((100+item.getTaxrate())/100));
			}
			total = total + order.getInstallation() + order.getPacking() + order.getForwarding() - order.getLess();
			String Message = URLEncoder.encode("Thank you for choosing HESH. Your Invoice has been generated, Invoice No: "+order.getBranch().getBillPrefix()+Utils.padBillNo(order.getSaleid())
					+" Dt: "+Utils.convertToAppDateDDMMYY(order.getSalesdate())+" Qty: "+qty+" Amt: Rs. "+Utils.get2Decimal(total)+". Regards HESHST","UTF-8");
//			String inputLine;
			URL url;
			BufferedReader bf;
			if(!order.getCustomer().getMobile1().equals("")){
				url = new URL(AUTH_SMS_BASE_URL+"&To="+order.getCustomer().getMobile1()+"&Message="+Message);
				bf = new BufferedReader(new InputStreamReader(url.openStream()));
				/*while((inputLine = bf.readLine()) != null)
				System.out.println(inputLine);
			System.out.println(inputLine);*/
				bf.close();
			}
			if(!order.getCustomer().getMobile2().equals("")){
				url = new URL(AUTH_SMS_BASE_URL+"&To="+order.getCustomer().getMobile2()+"&Message="+Message);
				bf = new BufferedReader(new InputStreamReader(url.openStream()));
				/*while((inputLine = bf.readLine()) != null)
				System.out.println(inputLine);
			System.out.println(inputLine);*/
				bf.close();
			}
			msg.addNewMessage(new Message(SUCCESS, "Message Sent Successfully!!"));
		} catch (Exception e) {
			msg.addNewMessage(new Message(ERROR, e.getMessage()));
		}
		return msg;
	}
	private String getOutstandingReport(HttpServletRequest request) {
		String pageType = "outstandingRpt.jsp";
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String userId = request.getParameter("sUser");
		request.setAttribute(USER, userId);
		SalesModel[] outstandingBills = getCollectionBroker().getOutstandingRpt("",branch,userId);  //new SalesModel[0];
		if(outstandingBills.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "initOutstandingRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, outstandingBills);
			HashMap<Integer, Double> totalOutstanding = new HashMap<Integer, Double>(0);
			SalesModel temp = null;
			double roundOff = 0;
			for(int i=0; i<outstandingBills.length; i++){
				temp = outstandingBills[i];
				roundOff = (1-Utils.get2DecimalDouble((temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())%1)) ;
				if(totalOutstanding.get(temp.getCustomer().getCustomerId()) != null){
					totalOutstanding.put(temp.getCustomer().getCustomerId(), totalOutstanding.get(temp.getCustomer().getCustomerId())+(Utils.get2DecimalDouble(Utils.get2DecimalDouble(temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())+roundOff-temp.getPayAmt())));
				} else {
					totalOutstanding.put(temp.getCustomer().getCustomerId(), (Utils.get2DecimalDouble(Utils.get2DecimalDouble(temp.getTotalAmt()+temp.getInstallation()+temp.getPacking()+temp.getForwarding()-temp.getLess())+roundOff-temp.getPayAmt())));
				}
			}
			request.setAttribute("totalOutstandingList", totalOutstanding);
		}
		initialize(request);
		return pageType;
	}

	private void initialize(HttpServletRequest request) {
		AccessPointModel[] branches = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, branches);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		return;
	}

}

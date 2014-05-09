package com.en.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.EntryModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesItemModel;
import com.en.model.SalesModel;
import com.en.model.UserModel;
import com.en.util.DateUtil;
import com.en.util.EmailUtil;
import com.en.util.Utils;

public class SalesHandler extends Basehandler {

	public static String getPageName() {
		return SALES_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "sales.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_SALES)){
        	pageType = addSales(request);			
		} else if(strAction.equals(ADD_ORDER_SALES)){
        	pageType = addOrderSales(request);			
		} else if(strAction.equals(PRNT_SALES)){
			pageType = printSales(request);			
		} else if(strAction.equals(PRNT_DC)){
			pageType = printDC(request);			
		} else if(strAction.equals(INIT_EXPT_DATA)){
			pageType = "exportSales.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			pageType = exportItemData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_SALES)){
			getTodayBillDetails(request);
			pageType = "viewSales.jsp";			
		} else if(strAction.equals(GET_SALES)){
			pageType = getSalesDetails(request);			
		} else if(strAction.equals(EMAIL_SALES)){
			pageType = emailSalesDetails(request);			
		} else if(strAction.equals(CANCEL_SALES)){
			pageType = cancelSalesDetails(request);			
		} else if(strAction.equals(INIT_GET_SALES_DATE)){
			pageType = initializeEditSalesDate(request);			
		} else if(strAction.equals(GET_SALES_DATE)){
			pageType = getEditSalesDate(request);			
		} else if(strAction.equals(EDIT_SALES_DATE)){
			pageType = editSalesDate(request);			
		} else if(strAction.equals(INIT_EDIT_SALES)){
			pageType = initializeEdit(request);			
		} else if(strAction.equals(CNFM_EDIT_SALES)){
			pageType = confirmEdit(request);			
		} else if(strAction.equals(GET_ITM_CHECK_LIST)){
			pageType = salesItemCheckList(request);			
		} else if(strAction.equals(ACC_EDIT_SALES)){
			pageType = confirmAccEdit(request);			
		} else if(strAction.equals(EDIT_SALES)){
			pageType = editSales(request);		
			if(request.getParameter("ACC") != null && request.getParameter("ACC").equals("Y")){
				pageType = "showSalesApproval.jsp";
			}
		} else if(strAction.equals(EDIT_SALES_LR_DTLS)){
			pageType = editSalesLRDetails(request);			
		} else if(strAction.equals(INIT_EDIT_SALES_LR_DTLS)){
			pageType = initEditSalesLRDetails(request);			
		} else if(strAction.equals(EDIT_SALES_NO_LR_DTLS)){
			pageType = editSalesNoLRDetails(request);
		}else if(strAction.equals(GET_EDIT_SALES_LR_DTLS)){
			pageType = getEditSalesLRDetails(request);			
		} else if(strAction.equals(SEND_SMS)){
			pageType = sendSMS(request);
		} else if(strAction.equals(SEND_LR_SMS)){
			pageType = sendLRSMS(request);
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String editSalesNoLRDetails(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		getSalesBroker().updateSalesNoLRDtls(billNo);
		return initEditSalesLRDetails(request);
	}

	private String cancelSalesDetails(HttpServletRequest request) {
		String pageType = "editSalesDate.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		String accessId = request.getParameter("txtAccessId");
		MessageModel msg = getSalesBroker().cancelSales(billNo, accessId);
		if((msg.getMessages().get(0)).getType().equals(ALERT)){
			SalesModel model = getSalesBroker().getSalesDetails(billNo);
			request.setAttribute(FORM_DATA, model);
			initializeEditSalesDate(request);
		} else {
			getEditSalesDate(request);
		}
		request.setAttribute(MESSAGES, msg);
		return pageType;
	}

	private String addOrderSales(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		String orderno = request.getParameter("ORDER_NO");
		SalesModel model = populateDataModel(request);		
		MessageModel msg = getSalesBroker().addSales(model);
		if(msg.getMessages().size() > 1 && (msg.getMessages().get(1)).getType().equals(SUCCESS)){
			getOrderBroker().adjustPartBill(orderno, model.getItems());
			model = getSalesBroker().getSalesDetails((msg.getMessages().get(1)).getMsg());
			double advance = getLedgerAdjustmentBroker().getOrderAdvance(orderno);
			if(advance>0){
				SalesItemModel[] items = (SalesItemModel[])model.getItems().toArray(new SalesItemModel[0]);
				double total = 0;
				for(int i=0; i<items.length; i++){
					total = total + Utils.get2DecimalDouble(((100-items[i].getDisrate())/100)*items[i].getQty() * (items[i].getRate()+((items[i].getRate() * items[i].getTaxrate())/100)));
				}
				total = total + model.getInstallation() + model.getForwarding() + model.getPacking() - model.getLess();
				if(total<advance){
					advance = total;
				}
				EntryModel[] entry = new EntryModel[1];
				entry[0] = new EntryModel();
				entry[0].setBillNo(model.getSaleid());
				entry[0].setBillBranch(model.getBranch());
				entry[0].setAmount(advance);
				entry[0].setAdjAmt(0);
				getLedgerAdjustmentBroker().adjustOrderAdvanceBillPending(orderno, entry, model.getCustomer().getCustomerId()+"");
			}
		} else {
			pageType = "sales.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String sendLRSMS(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		MessageModel msg = new MessageModel();
		if(!model.getCustomer().getMobile1().equals("") || !model.getCustomer().getMobile2().equals(""))

			try{
				sendLRSMS(model);
				msg.addNewMessage(new Message(SUCCESS, "Message Sent Successfully!!"));
			} catch (Exception e) {
				msg.addNewMessage(new Message(ERROR, e.getMessage()));
			}
		else {
			msg.addNewMessage(new Message(ALERT, "Customer mobile no Doesnot exist."));
		}
		request.setAttribute(MESSAGES, msg);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}
	
	private void sendLRSMS(SalesModel model) throws Exception{
		ArrayList<SalesItemModel> items = model.getItems();
		Iterator<SalesItemModel> itrItem = items.iterator();
		SalesItemModel item = null;
		double qty = 0;
		double total = 0;
		while(itrItem.hasNext()){
			item = itrItem.next();
			qty = qty + item.getQty();
			total = total + ((item.getQty()*item.getRate())*((100-item.getDisrate())/100)*((100+item.getTaxrate())/100));
		}
		total = total + model.getInstallation() + model.getPacking() + model.getForwarding() - model.getLess();
		String Message = URLEncoder.encode("Your Inv No: "+model.getBranch().getBillPrefix()+Utils.padBillNo(model.getSaleid())+", send through "+model.getTransport()+" Lr No: "+model.getLrno()+" Dt: "+Utils.convertToAppDateDDMMYY(model.getLrdt())+", Ctns: "+model.getCtns()+". Regards HESHST","UTF-8");
//		String inputLine;
		URL url;
		BufferedReader bf;
		if(!model.getCustomer().getMobile1().equals("")){
			url = new URL(AUTH_SMS_BASE_URL+"&To="+model.getCustomer().getMobile1()+"&Message="+Message);
			bf = new BufferedReader(new InputStreamReader(url.openStream()));
			/*while((inputLine = bf.readLine()) != null)
				System.out.println(inputLine);
			System.out.println(inputLine);*/
			bf.close();
		}
		if(!model.getCustomer().getMobile2().equals("")){
			url = new URL(AUTH_SMS_BASE_URL+"&To="+model.getCustomer().getMobile2()+"&Message="+Message);
			bf = new BufferedReader(new InputStreamReader(url.openStream()));
			/*while((inputLine = bf.readLine()) != null)
				System.out.println(inputLine);
			System.out.println(inputLine);*/
			bf.close();
		}
	}

	private String sendSMS(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		MessageModel msg ;
		if(!model.getCustomer().getMobile1().equals("") || !model.getCustomer().getMobile2().equals(""))
			msg = sendSalesDtlsSMS(model);
		else {
			msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Customer mobile no Doesnot exist."));
		}
		request.setAttribute(MESSAGES, msg);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}
	
	protected MessageModel sendSalesDtlsSMS(SalesModel order){
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
				total = total + ((item.getQty()*item.getRate())*((100-item.getDisrate())/100)*((100+item.getTaxrate())/100));
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

	private String salesItemCheckList(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		HashMap<String, Integer> dtls = getSalesBroker().getSalesItemCheckList(billNo);
		request.setAttribute(FORM_DATA, dtls);
		request.setAttribute("billNo", billNo);
		return "printSalesBillCheckList.jsp";
	}

	private String confirmAccEdit(HttpServletRequest request) {
		String pageType = "editSales.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		request.setAttribute("ACC", "Y");
		return pageType;
	}

	private String emailSalesDetails(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		MessageModel msg = EmailUtil.sendSalesMail(model);
		request.setAttribute(MESSAGES, msg);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String editSalesDate(HttpServletRequest request) {
		String pageType = "editSalesDate.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		String salesDt = Utils.convertToSQLDate(request.getParameter("txtSalesDate"));
		String accessId = request.getParameter("txtAccessId");
		MessageModel msg = getSalesBroker().editSalesDate(billNo, salesDt, accessId);
		if((msg.getMessages().get(0)).getType().equals(SUCCESS)){
			SalesModel model = getSalesBroker().getSalesDetails(billNo);
			request.setAttribute(FORM_DATA, model);
			initializeEditSalesDate(request);
		} else {
			getEditSalesDate(request);
		}
		request.setAttribute(MESSAGES, msg);
		return pageType;
	}

	private String getEditSalesDate(HttpServletRequest request) {
		initializeEditSalesDate(request);
		getEditSalesLRDetails(request);
		return "editSalesDate.jsp";
	}

	private String initializeEditSalesDate(HttpServletRequest request) {
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return "editSalesDate.jsp";
	}

	private String getEditSalesLRDetails(HttpServletRequest request) {
		String pageType = "editSalesLR.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		if(model.getSaleid() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
		} else {
			request.setAttribute(FORM_DATA, model);
			UserModel[] users = getUserBroker().getSalemanUsers();
			request.setAttribute(USERS, users);
		}
		return pageType;
	}

	private String initEditSalesLRDetails(HttpServletRequest request) {
		SalesModel[] lst = getSalesBroker().getPendingSalesLRList((AccessPointModel) request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute("SALES_DATA", lst);
		String pageType = "editSalesLR.jsp";
		return pageType;
	}

	private String editSalesLRDetails(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		String tin = request.getParameter("txtTIN");
		String cst = request.getParameter("txtCST");
		String mobile1 = request.getParameter("txtMobile1");
		String mobile2 = request.getParameter("txtMobile2");
		String email1 = request.getParameter("txtEmail1");
		String email2 = request.getParameter("txtEmail2");
		String custId = request.getParameter("txtCustId");
		String lrNo = request.getParameter("txtLRNo");
		String transport = request.getParameter("txtDespatch");
		String lrDt = Utils.convertToSQLDate(request.getParameter("txtLRDt"));
		String ctns = request.getParameter("txtCtns");
		String packedBy = request.getParameter("sPackedBy");
		String despatchedBy = request.getParameter("sDespatchedBy");
		getSalesBroker().updateCustomerDetails(custId, tin, cst, mobile1, mobile2, email1, email2);
		MessageModel msg = getSalesBroker().editSalesLRDetails(billNo, lrNo, lrDt, transport, packedBy, despatchedBy, ((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId(), ctns);
		if((msg.getMessages().get(0)).getType().equals(SUCCESS)){
			SalesModel model = getSalesBroker().getSalesDetails(billNo);
			request.setAttribute(FORM_DATA, model);
			try{
				if((new SimpleDateFormat("yyyy-MM-dd").parse("model.getSalesdate()")).getTime() > (new SimpleDateFormat("yyyy-MM-dd").parse("2013-03-31")).getTime()){
					sendLRSMS(model);
					EmailUtil.sendLREmail(model);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pageType = getEditSalesLRDetails(request);
		}
		return pageType;
	}

	private void getTodayBillDetails(HttpServletRequest request) {
		SalesModel[] model = getSalesBroker().getTodaySalesDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);	
		return;
	}

	private String printDC(HttpServletRequest request) {
		String pageType = "printDC.jsp";
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String editSales(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		SalesModel model = populateDataModel(request);		
		MessageModel msg = getSalesBroker().editSales(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getSalesBroker().getSalesDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editSales.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String confirmEdit(HttpServletRequest request) {
		String pageType = "editSales.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		String pass = request.getParameter("txtPassword");
		if(pass.equals(Utils.editSalesPass)){
			SalesModel model = getSalesBroker().getSalesDetails(billNo);
			if(model.getSaleid() == 0){
				MessageModel msg = new MessageModel();
				msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
				request.setAttribute(MESSAGES, msg);
				pageType = "editConfirm.jsp";
			} else {
				request.setAttribute(FORM_DATA, model);
				initialize(request);
			}
			Utils.editSalesPass = Utils.getRandomPassword();
		} else {
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "Password Incorrect."));
			request.setAttribute(MESSAGES, msg);
			pageType = "editConfirm.jsp";
			SalesModel model = getSalesBroker().getSalesDetails(billNo);
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String initializeEdit(HttpServletRequest request) {
		String pageType = "editConfirm.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String getSalesDetails(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		if(model.getSaleid() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			SalesModel[] models = getSalesBroker().getTodaySalesDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
			request.setAttribute(FORM_DATA, models);	
			pageType = "viewSales.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String exportItemData(HttpServletRequest request) {
		String pageType = "exportItemData.jsp";
		SalesItemModel[] model = getSalesBroker().getExportSalesItems(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String exportMasterData(HttpServletRequest request) {
		String pageType = "exportMasterData.jsp";
		SalesModel[] model = getSalesBroker().getExportSalesMaster(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String printSales(HttpServletRequest request) {
		String pageType = "printSalesBill.jsp";
		int copy = Integer.parseInt(request.getParameter("copies"));
		String[] copies = null;
		if(copy == 0){
			copies = (String[]) new String[] {"ORIGINAL","TRANSPORT COPY","OFFICE COPY"};
		} else if(copy == 1){
			copies = (String[]) new String[] {"ORIGINAL"};
		} else if(copy == 2){
			copies = (String[]) new String[]  {"TRANSPORT COPY"};
		} else {
			copies = (String[]) new String[] {"OFFICE COPY"};
		}
		request.setAttribute("copy", copies);	
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addSales(HttpServletRequest request) {
		String pageType = "showSales.jsp";
		SalesModel model = populateDataModel(request);		
		int alert = model.getAlert();
		double alertDis = model.getAlertDis();
		MessageModel msg = getSalesBroker().addSales(model);
		if(msg.getMessages().size() > 1 && (msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getSalesBroker().getSalesDetails((msg.getMessages().get(1)).getMsg());
			if(alert==1){
				try {
					String Message = "BRANCH : "+((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessName()
							+" INV. NO : "+((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getBillPrefix()+Utils.padBillNo(model.getSaleid())+
							" DISCOUNT : "+alertDis+"%. PLEASE CHECK HESHST";
					URL url;
					BufferedReader bf;
					String inputLine;
//					String[] nos = {"9884320072"};
					System.out.println(AUTH_SMS_BASE_URL_BULK+"&To=9884052555,9384052555,9382152555&Message="+URLEncoder.encode(Message, "ISO-8859-1"));
					url = new URL(AUTH_SMS_BASE_URL_BULK+"&To=9884052555,9384052555,9382152555&Message="+URLEncoder.encode(Message, "ISO-8859-1"));
					bf = new BufferedReader(new InputStreamReader(url.openStream()));
					while((inputLine = bf.readLine()) != null)
						System.out.println(inputLine);
					bf.close();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} else {
			pageType = "sales.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private SalesModel populateDataModel(HttpServletRequest request) {
		SalesModel model = new SalesModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("txtTin") != null && !request.getParameter("txtTin").trim().equals(""))
			model.getCustomer().setTin(request.getParameter("txtTin"));
		if(request.getParameter("txtCst") != null && !request.getParameter("txtCst").trim().equals(""))
			model.getCustomer().setCst(request.getParameter("txtCst"));
		if(request.getParameter("txtContactPerson") != null && !request.getParameter("txtContactPerson").trim().equals(""))
			model.getCustomer().setContactPerson(request.getParameter("txtContactPerson"));
		if(request.getParameter("txtMobile1") != null && !request.getParameter("txtMobile1").trim().equals(""))
			model.getCustomer().setMobile1(request.getParameter("txtMobile1"));
		if(request.getParameter("sUser") != null && !request.getParameter("sUser").trim().equals(""))
			model.getSalesman().setUserId(request.getParameter("sUser"));
		if(request.getParameter("sPackedBy") != null && !request.getParameter("sPackedBy").trim().equals(""))
			model.getPackedBy().setUserId(request.getParameter("sPackedBy"));
		if(request.getParameter("txtCtns") != null && !request.getParameter("txtCtns").trim().equals(""))
			model.setCtns(Integer.parseInt(request.getParameter("txtCtns")));
		if(request.getParameter("sDespatchedBy") != null && !request.getParameter("sDespatchedBy").trim().equals(""))
			model.getDespatchedBy().setUserId(request.getParameter("sDespatchedBy"));
		if(request.getParameter("rPayMode") != null && !request.getParameter("rPayMode").trim().equals(""))
			model.setPaymode(Integer.parseInt(request.getParameter("rPayMode")));
		if(request.getParameter("rTaxType") != null && !request.getParameter("rTaxType").trim().equals(""))
			model.setTaxtype(Integer.parseInt(request.getParameter("rTaxType")));
		if(request.getParameter("txtCreditDays") != null && !request.getParameter("txtCreditDays").trim().equals(""))
			model.setCreditDays(Integer.parseInt(request.getParameter("txtCreditDays")));
		if(request.getParameter("txtPacking") != null && !request.getParameter("txtPacking").trim().equals(""))
			model.setPacking(Double.parseDouble(request.getParameter("txtPacking")));
		if(request.getParameter("txtForwarding") != null && !request.getParameter("txtForwarding").trim().equals(""))
			model.setForwarding(Double.parseDouble(request.getParameter("txtForwarding")));
		if(request.getParameter("txtInstall") != null && !request.getParameter("txtInstall").trim().equals(""))
			model.setInstallation(Double.parseDouble(request.getParameter("txtInstall")));
		if(request.getParameter("txtLess") != null && !request.getParameter("txtLess").trim().equals(""))
			model.setLess(Double.parseDouble(request.getParameter("txtLess")));
		if(request.getParameter("txtDespatch") != null && !request.getParameter("txtDespatch").trim().equals(""))
			model.setTransport(request.getParameter("txtDespatch"));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemarks(request.getParameter("txtRemarks"));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setSaleid(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(3)));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.getBranch().setBillPrefix(request.getParameter("txtInvoiceNo").substring(0,3));
//		if(model.getOfferType().equals("1") && request.getParameter("txtOfferItems") != null && !request.getParameter("txtOfferItems").trim().equals("")){
			String salesItems = request.getParameter("txtSalesItems");
			StringTokenizer stRow = new StringTokenizer(salesItems,"#");
			String desc = "";
			double qty = 0;
			double rate = 0;
			double discount = 0;
			while (stRow.hasMoreElements()) {
				String tempRow = stRow.nextToken();
				StringTokenizer stCell = new StringTokenizer(tempRow,"|");
				ItemModel it = new ItemModel();
				it.setItemId(Integer.parseInt(stCell.nextToken()));
				it.setItemNumber(stCell.nextToken());
				desc = stCell.nextToken();
				qty = Double.parseDouble(stCell.nextToken());
				rate = Double.parseDouble(stCell.nextToken());
				discount = Double.parseDouble(stCell.nextToken());
				model.addItem(it, desc, qty, rate, discount);
				if(discount>10){
					model.setAlert(1);
					if(discount>model.getAlertDis()){
						model.setAlertDis(discount);
					}
				}
			}
//		}
		model.getBranch().setAccessId(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
		return model;
	}

	private void initialize(HttpServletRequest request) {
//		CustomerModel[] customers = getSalesBroker().getSalesCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		ItemModel[] items = getItemBroker().getSalesItem();
		request.setAttribute(ITEMS, items);
		String lastBill = getSalesBroker().getLastBillNo((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute(LAST_BILL, lastBill);
		return;
	}

}

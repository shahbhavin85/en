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
import com.en.model.EntryModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OrderModel;
import com.en.model.PurchaseItemModel;
import com.en.model.PurchaseModel;
import com.en.model.SalesItemModel;
import com.en.model.SalesModel;
import com.en.model.TransferModel;
import com.en.model.UserModel;
import com.en.util.Constant;
import com.en.util.DateUtil;
import com.en.util.EmailUtil;
import com.en.util.Utils;

public class OrderHandler extends Basehandler {

	public static String getPageName() {
		return ORDER_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "order.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_ORDER)){
        	pageType = addOrder(request);			
		} else if(strAction.equals(CANCEL_ORDER)){
			pageType = cancelOrder(request);
		} else if(strAction.equals(COMPLETE_ORDER)){
			pageType = completeOrder(request);
		} else if(strAction.equals(PRNT_ORDER)){
			pageType = printOrder(request);			
		} else if(strAction.equals(PRNT_DC)){
			pageType = printDC(request);			
		} else if(strAction.equals(INIT_EXPT_DATA)){
			pageType = "exportPurchase.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			pageType = exportItemData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_ORDER)){
			getTodayOrderDetails(request);
			pageType = "viewOrder.jsp";			
		} else if(strAction.equals(GET_ORDER)){
			pageType = getOrderDetails(request);			
		} else if(strAction.equals(INIT_EDIT_ORDER)){
			pageType = initEdit(request);			
		} else if(strAction.equals(INIT_FOLLOWUP_1)){
			pageType = getOrderDetailsForFollowup(request);
		} else if(strAction.equals(FOLLOWUP)){
			pageType = getAddRemark(request);
		} else if(strAction.equals(TRANSFER_FOLLOWUP)){
			pageType = getAddTransferRemark(request);
		} else if(strAction.equals(INIT_TRANSFER_FOLLOWUP)){
			pageType = getTransferDetailsForFollowup(request);
		} else if(strAction.equals(GET_FOLLOWUP_DTLS)){
			pageType = showFollowUpDtls(request);
		} else if(strAction.equals(GET_TRANSFER_FOLLOWUP_DTLS)){
			pageType = showTransferFollowUpDtls(request);
		} else if(strAction.equals(INIT_FOLLOWUP)){
			pageType = initializeFollowup(request);			
		} else if(strAction.equals(INIT_ITEM_FOLLOWUP)){
			pageType = initializeItemFollowup(request);			
		} else if(strAction.equals(INIT_CUSTOMER_FOLLOWUP)){
			pageType = initializeCustomerFollowup(request);			
		} else if(strAction.equals(EDIT_ORDER)){
			pageType = editOrder(request);			
		} else if(strAction.equals(SHOW_ORDER)){
			getOrderDetails(request);
			pageType = "viewOrderDtls.jsp";
		} else if(strAction.equals(ORDER_REQUEST)) {
			pageType = orderRequest(request);
		} else if(strAction.equals(EMAIL_ORDER)){
			pageType = emailOrderDetails(request);			
		} else if(strAction.equals(CONVERT_TO_SALES)){
			pageType = convertToOrder(request);
		} else if(strAction.equals(CONVERT_TO_TRANSFER)){
			pageType = convertToTransfer(request);
		} else if(strAction.equals(MULTI_EMAIL)){
			pageType = sendMultiEmail(request);
		} else if(strAction.equals(SEND_SMS)){
			pageType = sendSMS(request);
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String initializeCustomerFollowup(HttpServletRequest request) {
		HashMap<String, ArrayList<OrderModel>> model = getOrderBroker().getFollowupCustomerDetails((String)request.getSession().getAttribute(Constant.ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);
		return "orderCustomerFollowup.jsp";
	}

	private String convertToTransfer(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		TransferModel st = getTransferRequestBroker().getTranferRequestDetails(billNo);
		st.setRemark("Stock Transfer For Request No :"+st.getFromBranch().getBillPrefix()+"TR"+Utils.padBillNo(st.getTransferid())+"\n"+st.getRemark());
		request.setAttribute(FORM_DATA, st);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		ItemModel[] items = getItemBroker().getTransferItem();
		request.setAttribute(ITEMS, items);
		request.setAttribute("ORDER_NO", billNo);
		return "orderToTransfer.jsp";
	}

	private String showTransferFollowUpDtls(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		request.setAttribute("billNo", billNo);
		OrderModel[] model = getOrderBroker().getTransferFollowupDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return "viewOrderFollowupDtls.jsp";
	}

	private String getTransferDetailsForFollowup(HttpServletRequest request) {
		String pageType = "transferFollowup.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		TransferModel model = getTransferRequestBroker().getTranferRequestDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String getAddTransferRemark(HttpServletRequest request) {
		String billNos = request.getParameter("chkBill");
		String followupDt = request.getParameter("txtFollowDate");
		String followupRemark = request.getParameter("txtRemarks");
		getOrderBroker().addTransferFollowupRemarks(billNos, followupDt, followupRemark, ((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId());
		return initializeFollowup(request);
	}

	private String orderRequest(HttpServletRequest request) {
		String[] items = request.getParameterValues("items");
		TransferModel model = new TransferModel();
		ItemModel item = null;
		for(int i=0; i<items.length; i++){
			item = new ItemModel();
			item.setItemId(Integer.parseInt(items[i]));
			item.setItemNumber(request.getParameter("number"+items[i]));
			model.addItem(item, "", Double.parseDouble(request.getParameter("txt"+items[i])));
		}
		request.setAttribute(FORM_DATA, model);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		ItemModel[] vItems = getItemBroker().getTransferItem();
		request.setAttribute(ITEMS, vItems);
		return "transferRequest.jsp";
	}

	private String initializeItemFollowup(HttpServletRequest request) {
		HashMap<String, Object> model = getOrderBroker().getFollowupItemDetails((String)request.getSession().getAttribute(Constant.ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model.get(FORM_DATA));
		request.setAttribute("MASTER_DTLS", model.get("MASTER_DTLS"));
		HashMap<Integer, Double> current = getOrderBroker().getCurrentStockDtls((String)request.getSession().getAttribute(Constant.ACCESS_POINT));	
		request.setAttribute(CURRENT, current);
		HashMap<Integer, Double> requested = getOrderBroker().getRequestedDetails((String)request.getSession().getAttribute(Constant.ACCESS_POINT));
		request.setAttribute(REQUESTED, requested);
		return "orderItemFollowup.jsp";
	}

	private String completeOrder(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		getOrderBroker().completeOrder(billNo);
		return initializeFollowup(request);
	}

	private String sendSMS(HttpServletRequest request) {
		String pageType = "showOrder.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		MessageModel msg;
		OrderModel model = getOrderBroker().getOrderDetails(billNo);
		if(!model.getCustomer().getMobile1().equals("") || !model.getCustomer().getMobile2().equals(""))
			msg = sendOrderDtlsSMS(model);
		else {
			msg = new MessageModel();
			msg.addNewMessage(new Message(ALERT, "Customer mobile no Doesnot exist."));
		}
		request.setAttribute(MESSAGES, msg);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}
	
	private MessageModel sendOrderDtlsSMS(OrderModel order){
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
			String Message = URLEncoder.encode("Thank you for choosing HESH. Your Order has been generated, Order No: "+order.getBranch().getBillPrefix()+"O"+Utils.padBillNo(order.getOrderId())
						+" Dt: "+Utils.convertToAppDateDDMMYY(order.getOrderDate())+" Qty: "+qty+" Amt: Rs. "+Utils.get2Decimal(total)+". Regards HESHST","UTF-8");
//			System.out.println(AUTH_SMS_BASE_URL+"&To="+order.getCustomer().getMobile1()+"&Message="+Message);
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

	private String sendMultiEmail(HttpServletRequest request) {
		
		String[] orderNos = request.getParameterValues("chkEmail");
		MessageModel messages = new MessageModel();
		MessageModel msg = null;
		
		for(int i=0; i<orderNos.length; i++){
			EntryModel[] advances = getOrderBroker().getAdvancePayDtls(orderNos[i]);
			OrderModel model = getOrderBroker().getOrderDetails(orderNos[i]);
			msg = EmailUtil.sendOrderMail(model, advances,"");
			if(msg.getMessages().size() > 0 && msg.getMessages().get(0).getType().equals(SUCCESS)){
				getOrderBroker().updateSentEmail(orderNos[i]);
			}
			messages.addNewMessage(msg.getMessages().get(0));
		}
		
		request.setAttribute(MESSAGES, messages);
		
		if(((String)request.getSession().getAttribute(ACCESS_POINT)).equals("3")){
			OrderModel[] model = getOrderBroker().getExhibitionOrderDetails();
			request.setAttribute(FORM_DATA, model);
		} else {
			OrderModel[] model = getOrderBroker().getTodayOrderDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));
			request.setAttribute(FORM_DATA, model);
		}
		return "viewOrder.jsp";
	}

	private String emailOrderDetails(HttpServletRequest request) {
		String pageType = "showOrder.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		String complete = request.getParameter("txtComplete");
		EntryModel[] advances = getOrderBroker().getAdvancePayDtls(billNo);
		OrderModel model = getOrderBroker().getOrderDetails(billNo);
		MessageModel msg = EmailUtil.sendOrderMail(model, advances, complete);
		if(msg.getMessages().size() > 0){
			getOrderBroker().updateSentEmail(billNo);
		}
		request.setAttribute(MESSAGES, msg);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String cancelOrder(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		MessageModel msg = getOrderBroker().cancelOrder(billNo);
		request.setAttribute(MESSAGES, msg);
		return initializeFollowup(request);
	}

	private String convertToOrder(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		OrderModel model = getOrderBroker().getOrderDetails(billNo);
		SalesModel sales = new SalesModel();
		sales.setCustomer(model.getCustomer());
		sales.setItems(model.getItems());
		sales.setInstallation(model.getInstallation());
		sales.setPacking(model.getPacking());
		sales.setForwarding(model.getForwarding());
		sales.setLess(model.getLess());
		sales.setTaxtype(model.getTaxtype());
		sales.setSalesman(model.getSalesman());
		sales.setRemarks("Sales Bill For OrderNo :"+model.getBranch().getBillPrefix()+"O"+Utils.padBillNo(model.getOrderId())+"\n"+model.getRemarks());
		request.setAttribute(FORM_DATA, sales);
//		CustomerModel[] customers = getSalesBroker().getSalesCustomer();
////		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		ItemModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		String lastBill = getSalesBroker().getLastBillNo((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute(LAST_BILL, lastBill);
		request.setAttribute("ORDER_NO", billNo);
		return "orderToSales.jsp";
	}

	private String initializeFollowup(HttpServletRequest request) {
		OrderModel[] model = getOrderBroker().getFollowupOrderDetails((String)request.getSession().getAttribute(Constant.ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);
		TransferModel[] branchRequest = getTransferRequestBroker().getFollowupRequestDetails((String)request.getSession().getAttribute(Constant.ACCESS_POINT));	
		request.setAttribute(TRANSFER_REQUEST_DATA, branchRequest);
		return "orderFollowup.jsp";
	}

	private String showFollowUpDtls(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		request.setAttribute("billNo", billNo);
		OrderModel[] model = getOrderBroker().getFollowupDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return "viewOrderFollowupDtls.jsp";
	}

	private String getAddRemark(HttpServletRequest request) {
		String billNos = request.getParameter("chkBill");
		String followupDt = request.getParameter("txtFollowDate");
		String followupRemark = request.getParameter("txtRemarks");
		getOrderBroker().addFollowupRemarks(billNos, followupDt, followupRemark, ((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId());
		if(((String)request.getSession().getAttribute(ACCESS_POINT)).equals("3")){
			OrderModel[] model = getOrderBroker().getFollowupExhibitionOrderDetails();	
			request.setAttribute(FORM_DATA, model);
			return "orderFollowup.jsp";
		}
		return initializeFollowup(request);
	}

	private String getOrderDetailsForFollowup(HttpServletRequest request) {
		String pageType = "orderFollowup1.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		OrderModel model = getOrderBroker().getOrderDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private void getTodayOrderDetails(HttpServletRequest request) {
		OrderModel[] model = getOrderBroker().getTodayOrderDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);
		return;
	}

	private String printDC(HttpServletRequest request) {
		String pageType = "printDC.jsp";
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String editOrder(HttpServletRequest request) {
		String pageType = "showOrder.jsp";
		OrderModel model = populateDataModel(request);		
		MessageModel msg = getOrderBroker().editOrder(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getOrderBroker().getOrderDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editOrder.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initEdit(HttpServletRequest request) {
		String pageType = "editOrder.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		OrderModel model = getOrderBroker().getOrderDetails(billNo);
		if(model.getOrderId() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewOrder.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
			initialize(request);
		}
		return pageType;
	}

	private String getOrderDetails(HttpServletRequest request) {
		String pageType = "showOrder.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		OrderModel model = getOrderBroker().getOrderDetails(billNo);
		if(model.getOrderId()== 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			getTodayOrderDetails(request);
			pageType = "viewOrder.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
			String email = request.getParameter("email");
			if(email != null && email.equals("Y")){
				emailOrderDetails(request);
			}
			String sms = request.getParameter("sms");
			if(sms != null && sms.equals("Y")){
				MessageModel msg;
				msg = sendOrderDtlsSMS(model);
				request.setAttribute(MESSAGES, msg);
			}
		}
		return pageType;
	}

	private String exportItemData(HttpServletRequest request) {
		String pageType = "exportPurchaseItem.jsp";
		PurchaseItemModel[] model = getPurchaseBroker().getExportPurchaseItems(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String exportMasterData(HttpServletRequest request) {
		String pageType = "exportPurchaseMaster.jsp";
		PurchaseModel[] model = getPurchaseBroker().getExportPurchaseMaster(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String printOrder(HttpServletRequest request) {
		String pageType = "printOrder.jsp";
		int copy = Integer.parseInt(request.getParameter("copies"));
		String[] copies = null;
		if(copy == 0){
			copies = (String[]) new String[] {"ORIGINAL","OFFICE COPY"};
		} else if(copy == 1){
			copies = (String[]) new String[] {"ORIGINAL"};
		} else {
			copies = (String[]) new String[] {"OFFICE COPY"};
		}
		request.setAttribute("complete",(request.getParameter("complete") != null && request.getParameter("complete").equals("Y")) ? "Y" : "N"); 
		request.setAttribute("copy", copies);	
		OrderModel model = getOrderBroker().getOrderDetails(request.getParameter("billNo"));	
		EntryModel[] advances = getOrderBroker().getAdvancePayDtls(request.getParameter("billNo"));
		request.setAttribute("advances", advances);
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addOrder(HttpServletRequest request) {
		String pageType = "showOrder.jsp";
		OrderModel model = populateDataModel(request);		
		MessageModel msg = getOrderBroker().addOrder(model);
		if(msg.getMessages().size()>1 && (msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getOrderBroker().getOrderDetails((msg.getMessages().get(1)).getMsg());
			if(request.getParameter("quotationId") != null && !request.getParameter("quotationId").equals("")){
				getQuotationBroker().closeOrderQuotation(request.getParameter("quotationId"), model.getOrderId());
			}
		} else {
			pageType = "order.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private OrderModel populateDataModel(HttpServletRequest request) {
		OrderModel model = new OrderModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("sUser") != null && !request.getParameter("sUser").trim().equals(""))
			model.getSalesman().setUserId(request.getParameter("sUser"));
		if(request.getParameter("rTaxType") != null && !request.getParameter("rTaxType").trim().equals(""))
			model.setTaxtype(Integer.parseInt(request.getParameter("rTaxType")));
		if(request.getParameter("txtPacking") != null && !request.getParameter("txtPacking").trim().equals(""))
			model.setPacking(Double.parseDouble(request.getParameter("txtPacking")));
		if(request.getParameter("txtForwarding") != null && !request.getParameter("txtForwarding").trim().equals(""))
			model.setForwarding(Double.parseDouble(request.getParameter("txtForwarding")));
		if(request.getParameter("txtInstall") != null && !request.getParameter("txtInstall").trim().equals(""))
			model.setInstallation(Double.parseDouble(request.getParameter("txtInstall")));
		if(request.getParameter("txtLess") != null && !request.getParameter("txtLess").trim().equals(""))
			model.setLess(Double.parseDouble(request.getParameter("txtLess")));
		if(request.getParameter("txtAdvance") != null && !request.getParameter("txtAdvance").trim().equals(""))
			model.setAdvance(Double.parseDouble(request.getParameter("txtAdvance")));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemarks(request.getParameter("txtRemarks"));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setOrderId(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(4)));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.getBranch().setBillPrefix(request.getParameter("txtInvoiceNo").substring(0,3));
		if(request.getParameter("sPriority") != null && !request.getParameter("sPriority").trim().equals(""))
			model.setPriority(Integer.parseInt(request.getParameter("sPriority")));
		if(request.getParameter("txtDevDays") != null && !request.getParameter("txtDevDays").trim().equals(""))
			model.setDevDays(Integer.parseInt(request.getParameter("txtDevDays")));
		if(request.getParameter("txtFormNo") != null && !request.getParameter("txtFormNo").trim().equals(""))
			model.setFormNo(request.getParameter("txtFormNo"));
		if(request.getParameter("txtDevAdd") != null && !request.getParameter("txtDevAdd").trim().equals(""))
			model.setDevAddress(request.getParameter("txtDevAdd"));
		if(request.getParameter("rPayType") != null && !request.getParameter("rPayType").trim().equals(""))
			model.setPayType(Integer.parseInt(request.getParameter("rPayType")));
		if(request.getParameter("txtCredDays") != null && !request.getParameter("txtCredDays").trim().equals(""))
			model.setCredDays(Integer.parseInt(request.getParameter("txtCredDays")));
		if(request.getParameter("txtEMINo") != null && !request.getParameter("txtEMINo").trim().equals(""))
			model.setEMINo(Integer.parseInt(request.getParameter("txtEMINo")));
		if(request.getParameter("txtEMIDays") != null && !request.getParameter("txtEMIDays").trim().equals(""))
			model.setEMIDays(Integer.parseInt(request.getParameter("txtEMIDays")));
		if(request.getParameter("txtDownPay") != null && !request.getParameter("txtDownPay").trim().equals(""))
			model.setDownPay(Double.parseDouble(request.getParameter("txtDownPay")));
		if(request.getParameter("txtEMIAmt") != null && !request.getParameter("txtEMIAmt").trim().equals(""))
			model.setEMIAmt(Double.parseDouble(request.getParameter("txtEMIAmt")));
//		if(model.getOfferType().equals("1") && request.getParameter("txtOfferItems") != null && !request.getParameter("txtOfferItems").trim().equals("")){
			String salesItems = request.getParameter("txtSalesItems");
			StringTokenizer stRow = new StringTokenizer(salesItems,"#");
			while (stRow.hasMoreElements()) {
				String tempRow = stRow.nextToken();
				StringTokenizer stCell = new StringTokenizer(tempRow,"|");
				ItemModel it = new ItemModel();
				it.setItemId(Integer.parseInt(stCell.nextToken()));
				it.setItemNumber(stCell.nextToken());
				model.addItem(it, stCell.nextToken(), Double.parseDouble(stCell.nextToken()), Double.parseDouble(stCell.nextToken()), Double.parseDouble(stCell.nextToken()));
			}
//		}
		model.getBranch().setAccessId(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
		return model;
	}

	private void initialize(HttpServletRequest request) {
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		ItemModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		return;
	}

}
package com.en.handler;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.AccessPointBroker;
import com.en.broker.BranchDayEntryBroker;
import com.en.broker.DBConnection;
import com.en.model.AccessPointModel;
import com.en.model.ItemCategoryModel;
import com.en.model.MessageModel;

public class AccessPointHandler extends Basehandler {

	public static String getPageName() {
		return ACCESS_POINT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addAccessPoint.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_ACCESS_POINT)){
			addAccessPoint(request,response);			
		} else if(strAction.equals(MOD_ACCESS_POINT)){
			initializeModify(request);
			pageType = "modifyAccessPoint.jsp";			
		} else if(strAction.equals(UPDT_ACCESS_POINT)){
			updateAccessPoint(request);
			pageType = "modifyAccessPoint.jsp";			
		} else if(strAction.equals(GET_ACCESS_POINT)){
			getAccessPoint(request);
			pageType = "modifyAccessPoint.jsp";			
		} else if(strAction.equals(CASH_ON_HAND)){
			getAccessPointCOH(request);
			pageType = "cashonhand.jsp";			
		}
		return pageType;
	}
	
	public static double getTotalCashOnHand(HttpServletRequest request){
		double balance = 0;
		try{
			Connection conn = (new DBConnection()).getConnection();
			AccessPointBroker apBroker = new AccessPointBroker(conn);
			BranchDayEntryBroker bdBroker = new BranchDayEntryBroker(conn);
			AccessPointModel[] branches  = apBroker.getAccessPoint();
			for(int i=0; i<branches.length; i++){
				balance = balance + bdBroker.getBranchCashBalance(branches[i].getAccessId());
			}
			balance = balance + bdBroker.getBranchCashBalance(2);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

	private void getAccessPointCOH(HttpServletRequest request) {
		ArrayList<AccessPointModel> data = new ArrayList<AccessPointModel>(0);
		AccessPointModel temp = null;
		double openBal = 0;
		AccessPointModel[] branches  = getAccessPointBroker().getAccessPoint();
		for(int i=0; i<branches.length; i++){
			openBal = 0;
			openBal = getBranchDayEntryBroker().getBranchCashBalance(branches[i].getAccessId());
			temp = branches[i];
			temp.setOpenBal(openBal);
			data.add(temp);
		}
		openBal = getBranchDayEntryBroker().getBranchCashBalance(2);
		temp = new AccessPointModel();
		temp.setAccessId(2);
		temp.setAccessName("ACCOUNTS");
		temp.setOpenBal(openBal);
		data.add(temp);
		request.setAttribute(FORM_DATA, (AccessPointModel[]) data.toArray(new AccessPointModel[0]));
	}

	private void updateAccessPoint(HttpServletRequest request) {
		AccessPointBroker broker = getAccessPointBroker();
		AccessPointModel model = populateDataModel(request);
		MessageModel msgModel = broker.updateAccessPoint(model);
		request.setAttribute(MESSAGES, msgModel);
		model = broker.getAccessPointDtls(model.getAccessId());
		request.setAttribute(FORM_DATA, model);
		AccessPointModel[] accessPoints = broker.getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

	private void getAccessPoint(HttpServletRequest request) {
		AccessPointBroker broker = getAccessPointBroker();
		AccessPointModel model = broker.getAccessPointDtls(Integer.parseInt(request.getParameter("sAccessName")));
		request.setAttribute(FORM_DATA, model);
		AccessPointModel[] accessPoints = broker.getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		initialize(request);
		return;
	}

	private void initialize(HttpServletRequest request) {
		ItemCategoryModel[] itemCats = getItemCategoryBroker().getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		return;
	}

	private void initializeModify(HttpServletRequest request) {
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

	private void addAccessPoint(HttpServletRequest request,
			HttpServletResponse response) {
		AccessPointModel model = populateDataModel(request);
		MessageModel msgModel = getAccessPointBroker().addAccessPoint(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, model);
		return;
	}

	private AccessPointModel populateDataModel(HttpServletRequest request) {
		AccessPointModel model = new AccessPointModel();
		if(request.getParameter("txtAccessName") != null && !request.getParameter("txtAccessName").trim().equals(""))
			model.setAccessName(request.getParameter("txtAccessName"));
		if(request.getParameter("txtBillPre") != null && !request.getParameter("txtBillPre").trim().equals(""))
			model.setBillPrefix(request.getParameter("txtBillPre"));
		if(request.getParameter("taAddress") != null && !request.getParameter("taAddress").trim().equals(""))
			model.setAddress(request.getParameter("taAddress"));
		if(request.getParameter("txtCity") != null && !request.getParameter("txtCity").trim().equals(""))
			model.setCity(request.getParameter("txtCity"));
		if(request.getParameter("txtDistrict") != null && !request.getParameter("txtDistrict").trim().equals(""))
			model.setDistrict(request.getParameter("txtDistrict"));
		if(request.getParameter("txtState") != null && !request.getParameter("txtState").trim().equals(""))
			model.setState(request.getParameter("txtState"));
		if(request.getParameter("txtPinCode") != null && !request.getParameter("txtPinCode").trim().equals(""))
			model.setPincode(request.getParameter("txtPinCode"));
		if(request.getParameter("txtStdCode") != null && !request.getParameter("txtStdCode").trim().equals(""))
			model.setStdcode(request.getParameter("txtStdCode"));
		if(request.getParameter("txtPhone1") != null && !request.getParameter("txtPhone1").trim().equals(""))
			model.setPhone1(request.getParameter("txtPhone1"));
		if(request.getParameter("txtPhone2") != null && !request.getParameter("txtPhone2").trim().equals(""))
			model.setPhone2(request.getParameter("txtPhone2"));
		if(request.getParameter("txtMobile1") != null && !request.getParameter("txtMobile1").trim().equals(""))
			model.setMobile1(request.getParameter("txtMobile1"));
		if(request.getParameter("txtMobile2") != null && !request.getParameter("txtMobile2").trim().equals(""))
			model.setMobile2(request.getParameter("txtMobile2"));
		if(request.getParameter("txtMobile2") != null && !request.getParameter("txtMobile2").trim().equals(""))
			model.setMobile2(request.getParameter("txtMobile2"));
		if(request.getParameter("rFunctional") != null && !request.getParameter("rFunctional").trim().equals(""))
			model.setFunctional(request.getParameter("rFunctional"));
		if(request.getParameter("sAccessName") != null && !request.getParameter("sAccessName").trim().equals(""))
			model.setAccessId(Integer.parseInt(request.getParameter("sAccessName")));
		if(request.getParameter("txtTin") != null && !request.getParameter("txtTin").trim().equals(""))
			model.setVat(request.getParameter("txtTin"));
		if(request.getParameter("txtCst") != null && !request.getParameter("txtCst").trim().equals(""))
			model.setCst(request.getParameter("txtCst"));
		if(request.getParameter("txtEmail") != null && !request.getParameter("txtEmail").trim().equals(""))
			model.setEmail(request.getParameter("txtEmail"));
		if(request.getParameter("txtWithTin") != null && !request.getParameter("txtWithTin").trim().equals(""))
			model.setWithTin(request.getParameter("txtWithTin"));
		if(request.getParameter("txtNoTin") != null && !request.getParameter("txtNoTin").trim().equals(""))
			model.setNoTin(request.getParameter("txtNoTin"));
		if(request.getParameter("txtBankName1") != null && !request.getParameter("txtBankName1").trim().equals(""))
			model.setBankName1(request.getParameter("txtBankName1"));
		if(request.getParameter("txtBankBranch1") != null && !request.getParameter("txtBankBranch1").trim().equals(""))
			model.setBankBranch1(request.getParameter("txtBankBranch1"));
		if(request.getParameter("txtBankAc1") != null && !request.getParameter("txtBankAc1").trim().equals(""))
			model.setBankAc1(request.getParameter("txtBankAc1"));
		if(request.getParameter("txtBankIFSC1") != null && !request.getParameter("txtBankIFSC1").trim().equals(""))
			model.setBankIfsc1(request.getParameter("txtBankIFSC1"));
		if(request.getParameter("txtBankName2") != null && !request.getParameter("txtBankName2").trim().equals(""))
			model.setBankName2(request.getParameter("txtBankName2"));
		if(request.getParameter("txtBankBranch2") != null && !request.getParameter("txtBankBranch2").trim().equals(""))
			model.setBankBranch2(request.getParameter("txtBankBranch2"));
		if(request.getParameter("txtBankAc2") != null && !request.getParameter("txtBankAc2").trim().equals(""))
			model.setBankAc2(request.getParameter("txtBankAc2"));
		if(request.getParameter("txtBankIFSC2") != null && !request.getParameter("txtBankIFSC2").trim().equals(""))
			model.setBankIfsc2(request.getParameter("txtBankIFSC2"));
		return model;
	}

}

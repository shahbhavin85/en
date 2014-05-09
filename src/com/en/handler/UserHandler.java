package com.en.handler;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.en.broker.UserBroker;
import com.en.model.AccessPointModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TargetModel;
import com.en.model.UserModel;
import com.en.util.DateUtil;

public class UserHandler extends Basehandler {

	public static String getPageName() {
		return USER_HANDLER;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addUser.jsp";
		// action to taken
		String strAction = "";
		List<FileItem> list = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){
			list = (List<FileItem>) request.getAttribute(MUTLIPART_DATA);
			Iterator<FileItem> itr = list.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					if(name.equals(ACTIONS)){
						strAction = value;
						break;
					}
				} else {
					continue;
				}
			}
		} else {
			strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
		}
        if(strAction.equals(ADD_USER)){
			addUser(request,response);			
		} else if(strAction.equals(MOD_USER)){
			initialize(request);
			pageType = "modifyUser.jsp";			
		} else if(strAction.equals(UPDT_USER)){
			updateUser(request);
			pageType = "modifyUser.jsp";			
		} else if(strAction.equals(GET_USER)){
			getUser(request);
			pageType = "modifyUser.jsp";			
		} else if(strAction.equals(INIT_TARGET)){
			getInitTarget(request);
			pageType = "userTarget.jsp";			
		} else if(strAction.equals(PREV_TARGET)){
			getPrevTarget(request);
			pageType = "userTarget.jsp";			
		} else if(strAction.equals(NEXT_TARGET)){
			getNextTarget(request);
			pageType = "userTarget.jsp";			
		} else if(strAction.equals(SAVE_TARGET)){
			saveTarget(request);
			pageType = "userTarget.jsp";			
		} else if(strAction.equals(INIT_PASSWORD)){
			initialize(request);
			pageType = "resetPassword.jsp";
		} else if(strAction.equals(INIT_USER_DOC)){
			initialize(request);
			pageType = "userDocument.jsp";
		} else if(strAction.equals(GET_USER_DOC)){
			getUserDocumentList(request);
			initialize(request);
			pageType = "userDocument.jsp";
		} else if(strAction.equals(UPLOAD_USER_DOC)){
			uploadUserDocumentList(list,request);
			initialize(request);
			pageType = "userDocument.jsp";
		} else if(strAction.equals(UPDT_PASSWORD)){
			updatePassword(request);
			initialize(request);
			pageType = "resetPassword.jsp";
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void saveTarget(HttpServletRequest request) {
		String[] users = request.getParameterValues("users");
		String fromDate = request.getParameter("txtFromDate");
		String toDate = request.getParameter("txtToDate");
		ArrayList<TargetModel> lst = new ArrayList<TargetModel>();
		TargetModel temp = null;
		for(int i=0; i<users.length; i++){
				temp = new TargetModel();
				temp.getUser().setUserId(users[i]);
				temp.setTarget((request.getParameter("target"+users[i]) != null) ? Double.parseDouble(request.getParameter("target"+users[i])) : 0);
				temp.setCommission((request.getParameter("commission"+users[i]) != null) ? Double.parseDouble(request.getParameter("commission"+users[i])) : 0);
				lst.add(temp);
		}
		MessageModel msg = getUserBroker().updateTarget(lst, fromDate, toDate);
		request.setAttribute(MESSAGES, msg);
		TargetModel[] targets = getUserBroker().getUserTargets(request.getParameter("txtFromDate"), request.getParameter("txtToDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		request.setAttribute(FORM_DATA, targets);
		return;
	}

	private void getNextTarget(HttpServletRequest request) {
		Date dt = new Date();
		try {
			dt = (new SimpleDateFormat("yyyy-MM-dd")).parse(request.getParameter("txtToDate"));
			Calendar c = new GregorianCalendar();
			c.setTime(dt);
			c.setTimeInMillis(c.getTimeInMillis()+24*60*60*1000);
			String[] quarterDates = DateUtil.getCurrentQuarterDate(c);
			TargetModel[] targets = getUserBroker().getUserTargets(quarterDates[0], quarterDates[1]);
			request.setAttribute("txtFromDate", quarterDates[0]);
			request.setAttribute("txtToDate", quarterDates[1]);
			request.setAttribute(FORM_DATA, targets);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;}

	private void getPrevTarget(HttpServletRequest request) {
		Date dt = new Date();
		try {
			dt = (new SimpleDateFormat("yyyy-MM-dd")).parse(request.getParameter("txtFromDate"));
			Calendar c = new GregorianCalendar();
			c.setTime(dt);
			c.setTimeInMillis(c.getTimeInMillis()-24*60*60*1000);
			String[] quarterDates = DateUtil.getCurrentQuarterDate(c);
			TargetModel[] targets = getUserBroker().getUserTargets(quarterDates[0], quarterDates[1]);
			request.setAttribute("txtFromDate", quarterDates[0]);
			request.setAttribute("txtToDate", quarterDates[1]);
			request.setAttribute(FORM_DATA, targets);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	private void getInitTarget(HttpServletRequest request) {
		String[] quarterDates = DateUtil.getCurrentQuarterDate(new GregorianCalendar());
		TargetModel[] targets = getUserBroker().getUserTargets(quarterDates[0], quarterDates[1]);
		request.setAttribute("txtFromDate", quarterDates[0]);
		request.setAttribute("txtToDate", quarterDates[1]);
		request.setAttribute(FORM_DATA, targets);
		return;
	}

	private void uploadUserDocumentList(List<FileItem> items,
			HttpServletRequest request) {
		MessageModel msgs = new MessageModel();
		try{
			HashMap<String, String> reqMap = new HashMap<String, String>(0);
			String documentName = "";
			String fileName = "";
			String userId = "";

//			String imgDir = "E:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\PROMOTIONAL_IMAGES\\";
//			String imgDir = "E:\\EMAIL_FILES\\";
//			String imgDir = "G:\\EMAIL_FILES\\";
			
//			File dir = new File(imgDir);
//			if (!dir.exists()) {
//				dir.mkdir();
//			}
			Iterator<FileItem> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					reqMap.put(name, value);
					if(name.equals("taDescription")){
						documentName = value;
					}
					if(name.equals("txtUserId")){
						userId = value;
					}
				} else {
					if(item.getSize()>0){
//						item.get
//						if(item.getContentType().equals("application/pdf"))
//							fileName = "Email" + id + ".pdf";
//						if(item.getContentType().equals("application/jpeg"))
//							fileName = "Email" + id + ".jpg";
						fileName = item.getName();
						String dirStr = "webapps//USER_DOCS//";
//						String dirStr = "E://HESH_SOFTWARE_FILES//";
						File dir = new File(dirStr);
						if (!dir.exists()) 
						{
							dir.mkdir();
						}
//						dirStr = "E://HESH_SOFTWARE_FILES//CUSTOMER_MAIL_FILES//";
//						dir = new File(dirStr);
//						if (!dir.exists()) 
//						{
//							dir.mkdir();
//						}
						dirStr = dirStr+userId+"//";
						dir = new File(dirStr);
						if (!dir.exists()) 
						{
							dir.mkdir();
						}
						File saveFile = new File(dirStr+item.getName());
						item.write(saveFile);
					}
				}
			}
			
			getUserBroker().uploadUserDocument(userId, documentName, fileName);

			request.setAttribute(USER, userId);
			String[][] lst = getUserBroker().getUserDocumentList(userId);
			request.setAttribute(FORM_DATA, lst);
			
	        msgs.addNewMessage(new Message(SUCCESS, "Document uploaded successfully!!"));
	        
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while sending email!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
		
		return;
	}

	private void getUserDocumentList(HttpServletRequest request) {
		String userId = request.getParameter("txtUserId");
		request.setAttribute(USER, userId);
		String[][] lst = getUserBroker().getUserDocumentList(userId);
		request.setAttribute(FORM_DATA, lst);
		return;
	}

	private void initialize(HttpServletRequest request) {
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

	private void updatePassword(HttpServletRequest request) {
		String userId = request.getParameter("txtUserId");
		String pwd = request.getParameter("txtPassword");
		MessageModel msgModel = getUserBroker().updatePassword(userId,pwd);;
		request.setAttribute(MESSAGES, msgModel);
	}

	private void updateUser(HttpServletRequest request) {
		UserBroker broker = getUserBroker();
		UserModel model = populateDataModel(request);
		MessageModel msgModel = broker.updateUser(model);
		request.setAttribute(MESSAGES, msgModel);
		model = broker.getUserDtls(model.getUserId());
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		return;
	}

	private void getUser(HttpServletRequest request) {
		UserBroker broker = getUserBroker();
		UserModel model = broker.getUserDtls(request.getParameter("txtUserId"));
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		return;
	}

	private void addUser(HttpServletRequest request,
			HttpServletResponse response) {
		UserModel model = populateDataModel(request);
		MessageModel msgModel = getUserBroker().addUser
				(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, model);
		initialize(request);
		return;
	}

	private UserModel populateDataModel(HttpServletRequest request) {
		UserModel model = new UserModel();
		if(request.getParameter("txtUserId") != null && !request.getParameter("txtUserId").trim().equals(""))
			model.setUserId(request.getParameter("txtUserId"));
		if(request.getParameter("txtPassword") != null && !request.getParameter("txtPassword").trim().equals(""))
			model.setPassword(request.getParameter("txtPassword"));
		if(request.getParameter("txtFullName") != null && !request.getParameter("txtFullName").trim().equals(""))
			model.setUserName(request.getParameter("txtFullName"));

		if(request.getParameter("txtMobile1") != null && !request.getParameter("txtMobile1").trim().equals(""))
			model.setMobile1(request.getParameter("txtMobile1"));
		if(request.getParameter("txtPhone1") != null && !request.getParameter("txtPhone1").trim().equals(""))
			model.setPhone1(request.getParameter("txtPhone1"));
		if(request.getParameter("txtEmail1") != null && !request.getParameter("txtEmail1").trim().equals(""))
			model.setEmail1(request.getParameter("txtEmail1"));
		if(request.getParameter("txtSalary") != null && !request.getParameter("txtSalary").trim().equals(""))
			model.setSalary(request.getParameter("txtSalary"));

		if(request.getParameter("txtDOB") != null && !request.getParameter("txtDOB").trim().equals(""))
			model.setDob(request.getParameter("txtDOB"));
		if(request.getParameter("txtBloodGroup") != null && !request.getParameter("txtBloodGroup").trim().equals(""))
			model.setBloodGroup(request.getParameter("txtBloodGroup"));
		if(request.getParameter("txtMobile2") != null && !request.getParameter("txtMobile2").trim().equals(""))
			model.setMobile2(request.getParameter("txtMobile2"));
		if(request.getParameter("txtPhone2") != null && !request.getParameter("txtPhone2").trim().equals(""))
			model.setPhone2(request.getParameter("txtPhone2"));
		if(request.getParameter("txtEmail2") != null && !request.getParameter("txtEmail2").trim().equals(""))
			model.setEmail2(request.getParameter("txtEmail2"));
		if(request.getParameter("taQualification") != null && !request.getParameter("taQualification").trim().equals(""))
			model.setQualification(request.getParameter("taQualification"));
		if(request.getParameter("taIdentityMark") != null && !request.getParameter("taIdentityMark").trim().equals(""))
			model.setPersonalIdentityMark(request.getParameter("taIdentityMark"));
		if(request.getParameter("taPermanentAddress") != null && !request.getParameter("taPermanentAddress").trim().equals(""))
			model.setAddress(request.getParameter("taPermanentAddress"));
		if(request.getParameter("taPresentAddress") != null && !request.getParameter("taPresentAddress").trim().equals(""))
			model.setPresentAddress(request.getParameter("taPresentAddress"));
		if(request.getParameter("txtFatherName") != null && !request.getParameter("txtFatherName").trim().equals(""))
			model.setFatherName(request.getParameter("txtFatherName"));
		if(request.getParameter("txtMotherName") != null && !request.getParameter("txtMotherName").trim().equals(""))
			model.setMotherName(request.getParameter("txtMotherName"));
		if(request.getParameter("txtSpouseName") != null && !request.getParameter("txtSpouseName").trim().equals(""))
			model.setSpouseName(request.getParameter("txtSpouseName"));
		if(request.getParameter("txtDOA") != null && !request.getParameter("txtDOA").trim().equals(""))
			model.setDoa(request.getParameter("txtDOA"));
		if(request.getParameter("txtChild1") != null && !request.getParameter("txtChild1").trim().equals(""))
			model.setChild1(request.getParameter("txtChild1"));
		if(request.getParameter("txtChild2") != null && !request.getParameter("txtChild2").trim().equals(""))
			model.setChild2(request.getParameter("txtChild2"));
		if(request.getParameter("txtChild3") != null && !request.getParameter("txtChild3").trim().equals(""))
			model.setChild3(request.getParameter("txtChild3"));
		if(request.getParameter("txtChild4") != null && !request.getParameter("txtChild4").trim().equals(""))
			model.setChild4(request.getParameter("txtChild4"));
		if(request.getParameter("txtExpense1") != null && !request.getParameter("txtExpense1").trim().equals(""))
			model.setExpense1(Double.parseDouble(request.getParameter("txtExpense1")));
		if(request.getParameter("txtExpense2") != null && !request.getParameter("txtExpense2").trim().equals(""))
			model.setExpense2(Double.parseDouble(request.getParameter("txtExpense2")));
		if(request.getParameter("txtExpense3") != null && !request.getParameter("txtExpense3").trim().equals(""))
			model.setExpense3(Double.parseDouble(request.getParameter("txtExpense3")));
		if(request.getParameter("txtExpense4") != null && !request.getParameter("txtExpense4").trim().equals(""))
			model.setExpense4(Double.parseDouble(request.getParameter("txtExpense4")));
		if(request.getParameter("txtExpense5") != null && !request.getParameter("txtExpense5").trim().equals(""))
			model.setExpense5(Double.parseDouble(request.getParameter("txtExpense5")));
		

		if(request.getParameter("txtPastCompany") != null && !request.getParameter("txtPastCompany").trim().equals(""))
			model.setPastCompany(request.getParameter("txtPastCompany"));
		if(request.getParameter("txtPeriod") != null && !request.getParameter("txtPeriod").trim().equals(""))
			model.setPeriod(request.getParameter("txtPeriod"));
		if(request.getParameter("txtDetails") != null && !request.getParameter("txtDetails").trim().equals(""))
			model.setDetails(request.getParameter("txtDetails"));

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
		

		if(request.getParameter("txtManagerId") != null && !request.getParameter("txtManagerId").trim().equals("--"))
			model.setManager(request.getParameter("txtManagerId"));
		if(request.getParameter("sBranch") != null && !request.getParameter("sBranch").trim().equals("--"))
			model.getBranch().setAccessId(Integer.parseInt(request.getParameter("sBranch")));
		if(request.getParameter("rType") != null)
			model.setType(Integer.parseInt(request.getParameter("rType")));
		
		if(request.getParameter("txtStdCode") != null && !request.getParameter("txtStdCode").trim().equals(""))
			model.setStdcode(request.getParameter("txtStdCode"));
		return model;
	}

}

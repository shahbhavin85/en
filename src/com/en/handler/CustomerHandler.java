package com.en.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.en.broker.CustomerBroker;
import com.en.model.CustomerEmailModel;
import com.en.model.CustomerGroupModel;
import com.en.model.CustomerModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.UserModel;
import com.en.util.EmailUtil;

public class CustomerHandler extends Basehandler {

	public static String getPageName() {
		return CUSTOMER_HANDLER;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addCustomer.jsp";
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
		if(strAction.equals(ADD_CUSTOMER)){
			addCustomer(request, response);			
		} else if(strAction.equals(MOD_CUSTOMER)){
			initializeModify(request);
			pageType = "modifyCustomer.jsp";			
		} else if(strAction.equals(UPDT_CUSTOMER)){
			updateCustomer(request);
			pageType = "modifyCustomer.jsp";			
		} else if(strAction.equals(GET_CUSTOMER)){
			getCustomer(request);
			pageType = "modifyCustomer.jsp";			
		} else if(strAction.equals(INIT_CUSTOMER)){
			initialize(request);
		} else if(strAction.equals(INIT_CUSTOMER_MERGE)){
			pageType = "customerMerge.jsp";
			initializeMerge(request);
		} else if(strAction.equals(GET_CUSTOMER_LST)){
			pageType = "customerMerge.jsp";
			getCustomerList(request);
		} else if(strAction.equals(INIT_CUSTOMER_LABEL)){
			initializeLabel(request);
			pageType = "customerLabel.jsp";
		} else if(strAction.equals(PRINT_CUSTOMER_LABEL)){
			printLabel(request);
			pageType = "printCustomerLabel.jsp";
		} else if(strAction.equals(ADD_CITY)){
			addCity(request);
			pageType = "addCity.jsp";
		} else if(strAction.equals(GET_CITIES)){
			initializeGetCityList(request);
			pageType = "customerLabel.jsp";
		} else if(strAction.equals(INIT_VIEW_CUSTOMER)){
			initializeView(request);
			pageType = "viewCustomer.jsp";
		} else if(strAction.equals(VIEW_CUSTOMER)){
			getCustomer(request);
			pageType = "viewCustomer.jsp";
		} else if(strAction.equals(ADD_CUST_TYPE)){
			addCustomerType(request);
			pageType = "addCustomerType.jsp";
		} else if(strAction.equals(DEL_CUSTOMER)){
			deleteCustomer(request);
			pageType = "customerMerge.jsp";
		} else if(strAction.equals(MERGE_CUSTOMER_1)){
			getMergeData(request);
			pageType = "customerMerge1.jsp";
		} else if(strAction.equals(MERGE_CUSTOMER_2)){
			mergeCustomer(request);
			pageType = "customerMerge.jsp";
		} else if(strAction.equals(CHECK_CUSTOMER_1)){
			initializeMerge(request);
			pageType = "checkCustomer.jsp";
		} else if(strAction.equals(CHECK_CUSTOMER_2)){
			getCustomerList(request);
			pageType = "checkCustomer.jsp";
		} else if(strAction.equals(INIT_EMAIL_CUSTOMER)){
			pageType = "customerEmail.jsp";
		} else if(strAction.equals(GET_EMAIL_CUSTOMER)){
			getCustomer(request);
			pageType = "customerEmail.jsp";
		} else if(strAction.equals(INIT_VIEW_EMAIL_CUSTOMER)){
//			getCustomer(request);
			pageType = "viewCustomerEmail.jsp";
		} else if(strAction.equals(VIEW_EMAIL_CUSTOMER)){
			getCustomerEmail(request);
			pageType = "viewCustomerEmail.jsp";
		} else if(strAction.equals(EMAIL_CUSTOMER)){
			sendCustomerMail(list,request);
			pageType = "viewCustomerEmail.jsp";
		} else if(strAction.equals(RESEND_EMAIL_CUSTOMER)){
			resendCustomerMail(request);
//			getCustomerEmail(request);
			pageType = "viewCustomerEmail.jsp";
		} else {
			pageType = "addCity.jsp";
		}
		return pageType;
	}

	private String sendCustomerMail(List<FileItem> items, HttpServletRequest request) {
		MessageModel msgs = new MessageModel();
		try{
			HashMap<String, String> reqMap = new HashMap<String, String>(0);
			String subject = "";
			String email1 = "";
			String email2 = "";
			String mobile1 = "";
			String mobile2 = "";
			String message = "";
			String fileName = "";
			String custId = "";
			boolean isAttachment = false;

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
					if(name.equals("txtSubject")){
						subject = value;
					}
					if(name.equals("taMessage")){
						message = value;
					}
					if(name.equals("txtMobile1")){
						mobile1 = value;
					}
					if(name.equals("txtMobile2")){
						mobile2 = value;
					}
					if(name.equals("txtEmail1")){
						email1 = value;
					}
					if(name.equals("txtEmail2")){
						email2 = value;
					}
					if(name.equals("sCustomer")){
						custId = value;
					}
				} else {
					if(item.getSize()>0){
//						item.get
//						if(item.getContentType().equals("application/pdf"))
//							fileName = "Email" + id + ".pdf";
//						if(item.getContentType().equals("application/jpeg"))
//							fileName = "Email" + id + ".jpg";
						fileName = item.getName();
						String dirStr = "webapps//CUSTOMER_MAIL_FILES//";
						File dir = new File(dirStr);
						if (!dir.exists()) 
						{
							dir.mkdir();
						}
						File saveFile = new File(dirStr+item.getName());
						item.write(saveFile);
						isAttachment = true;
					}
				}
			}
			
			CustomerEmailModel model = new CustomerEmailModel();
			
			model.getCustomer().setCustomerId(Integer.parseInt(custId));
			model.getCustomer().setEmail(email1);
			model.getCustomer().setEmail1(email2);
			model.getCustomer().setMobile1(mobile1);
			model.getCustomer().setMobile2(mobile2);
			model.setSubject(subject);
			model.setMessage(message);
			model.setFilename(fileName);
			
			int refNo = getCustomerBroker().emailCustomer(custId, email1, email2, subject, message, fileName, mobile1, mobile2);
			
			model.setEmailNo(refNo);
			model.setMessage(model.getMessage()+"\n Email Reference No : "+refNo);
			
			if(isAttachment)
				msgs = EmailUtil.sendCustomerAttMail(model);
			else 
				msgs = EmailUtil.sendCustomerMail(model);

			String smsMobile = (!model.getCustomer().getMobile1().equals("")) ? model.getCustomer().getMobile1() : (!model.getCustomer().getMobile2().equals("")) ? model.getCustomer().getMobile2() : "";
			String smsEmail = (!model.getCustomer().getEmail().equals("")) ? model.getCustomer().getEmail() : (!model.getCustomer().getEmail1().equals("")) ? model.getCustomer().getEmail1() : "";
			
//			System.out.println(smsMobile+"::"+smsEmail);

			if(!smsMobile.equals("")){
				URL url; 
//				BufferedReader bf;
//				String inputLine;
//				System.out.println(AUTH_SMS_BASE_URL+"&To="+mobile1+"&Message="+URLEncoder.encode("Email is sent to your email id "+smsEmail+" regarding "+subject+". HESHST","UTF-8"));
				url = new URL(AUTH_SMS_BASE_URL+"&To="+smsMobile+"&Message="+URLEncoder.encode("Email is sent to your email id "+smsEmail+" regarding "+subject+". HESHST","UTF-8"));
				new BufferedReader(new InputStreamReader(url.openStream()));
//				while((inputLine = bf.readLine()) != null)
//				System.out.println(inputLine);
			}
	        
//	        initialize(request);
			
//	        msgs.addNewMessage(new Message(SUCCESS, "Email sent successfully!!!"));
	        
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while sending email!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
        
		return "customerEmail.jsp";
	}

	private void getCustomerEmail(HttpServletRequest request) {
		String emailNo = request.getParameter("txtRefNo");
		CustomerEmailModel map = getCustomerBroker().getEmailDtls(emailNo);
		if(map != null){
			int custId = map.getCustomer().getCustomerId();
			CustomerModel customer = getCustomerBroker().getCustomerDtls(custId);
			map.setCustomer(customer);
			request.setAttribute(FORM_DATA, map);
		}
	}

	private void mergeCustomer(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		ArrayList<String> custIds = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(ids,"|");
		String temp = "";
		String masterId = request.getParameter("custChk");
		while(st.hasMoreTokens()){
			temp = st.nextToken();
			if(temp.equals(masterId)){
				continue;
			}
			custIds.add(temp);
		}
		MessageModel msgs = getCustomerBroker().mergeCustomer((String[]) custIds.toArray(new String[0]), masterId);
		CustomerModel model = new CustomerModel();
		if(request.getParameter("txtName"+masterId) != null && !request.getParameter("txtName"+masterId).trim().equals(""))
			model.setCustomerName(request.getParameter("txtName"+masterId));
		if(request.getParameter("txtContactPerson"+masterId) != null && !request.getParameter("txtContactPerson"+masterId).trim().equals(""))
			model.setContactPerson(request.getParameter("txtContactPerson"+masterId));
		if(request.getParameter("taAddress"+masterId) != null && !request.getParameter("taAddress"+masterId).trim().equals(""))
			model.setAddress(request.getParameter("taAddress"+masterId));
		if(request.getParameter("txtPinCode"+masterId) != null && !request.getParameter("txtPinCode"+masterId).trim().equals(""))
			model.setPincode(request.getParameter("txtPinCode"+masterId));
		if(request.getParameter("txtStdCode"+masterId) != null && !request.getParameter("txtStdCode"+masterId).trim().equals(""))
			model.setStdcode(request.getParameter("txtStdCode"+masterId));
		if(request.getParameter("txtPhone1"+masterId) != null && !request.getParameter("txtPhone1"+masterId).trim().equals(""))
			model.setPhone1(request.getParameter("txtPhone1"+masterId));
		if(request.getParameter("txtPhone2"+masterId) != null && !request.getParameter("txtPhone2"+masterId).trim().equals(""))
			model.setPhone2(request.getParameter("txtPhone2"+masterId));
		if(request.getParameter("txtMobile1"+masterId) != null && !request.getParameter("txtMobile1"+masterId).trim().equals(""))
			model.setMobile1(request.getParameter("txtMobile1"+masterId));
		if(request.getParameter("txtMobile2"+masterId) != null && !request.getParameter("txtMobile2"+masterId).trim().equals(""))
			model.setMobile2(request.getParameter("txtMobile2"+masterId));
		if(request.getParameter("txtEmail"+masterId) != null && !request.getParameter("txtEmail"+masterId).trim().equals(""))
			model.setEmail(request.getParameter("txtEmail"+masterId));
		if(request.getParameter("txtEmail1"+masterId) != null && !request.getParameter("txtEmail1"+masterId).trim().equals(""))
			model.setEmail1(request.getParameter("txtEmail1"+masterId));
		if(request.getParameter("txtTin"+masterId) != null && !request.getParameter("txtTin"+masterId).trim().equals(""))
			model.setTin(request.getParameter("txtTin"+masterId));
		if(request.getParameter("txtCst"+masterId) != null && !request.getParameter("txtCst"+masterId).trim().equals(""))
			model.setCst(request.getParameter("txtCst"+masterId));
		if(request.getParameter("txtArea"+masterId) != null && !request.getParameter("txtArea"+masterId).trim().equals(""))
			model.setArea(request.getParameter("txtArea"+masterId));
		if(request.getParameter("txtOpenBal"+masterId) != null && !request.getParameter("txtOpenBal"+masterId).trim().equals(""))
			model.setOpenBal(Double.parseDouble(request.getParameter("txtOpenBal"+masterId)));
		model.setCustomerId(Integer.parseInt(masterId));
		getCustomerBroker().updateMergeCustomer(model);
		request.setAttribute(MESSAGES, msgs);
		getCustomerList(request);
	}

	private void getMergeData(HttpServletRequest request) {
		request.setAttribute("txtCity", request.getParameter("txtCity"));
		CustomerModel[] custLst = getCustomerBroker().getCustomerLst(request.getParameterValues("custChk"));
		request.setAttribute("custLst", custLst);
	}

	private void deleteCustomer(HttpServletRequest request) {
		MessageModel msgs = getCustomerBroker().deleteCustomer(Integer.parseInt(request.getParameter("txtCustId")));
		request.setAttribute(MESSAGES, msgs);
		getCustomerList(request);
	}

	private void getCustomerList(HttpServletRequest request) {
		request.setAttribute("txtCity", request.getParameter("txtCity"));
		CustomerModel[] custLst = getCustomerBroker().getCustomerLst(request.getParameter("txtCity"),"","");
		request.setAttribute("custLst", custLst);
		initializeMerge(request);
	}

	private void initializeMerge(HttpServletRequest request) {
		String[] cities = getCustomerBroker().getDistinctCityList();
		request.setAttribute(CITIES, cities);
	}

	private void addCustomerType(HttpServletRequest request) {	
		if(request.getParameter("hdnAdd")!=null && ((String)request.getParameter("hdnAdd")).equals("1")){
			String custType = request.getParameter("txtType");
			if(custType.equals("")){
				MessageModel msgs = new MessageModel();
				msgs.addNewMessage(new Message(ERROR, "Customer Type field cannot be blank"));
				request.setAttribute(MESSAGES, msgs);
				request.setAttribute("type", custType);
			} else {
				MessageModel msgs = getCustomerBroker().addCustomerType(custType.toUpperCase());
				request.setAttribute("type", custType.toUpperCase());
				request.setAttribute(MESSAGES, msgs);
			}
		}
	}

	private void initializeView(HttpServletRequest request) {
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		return;
	}

	private void printLabel(HttpServletRequest request) {
		CustomerModel[] custLst = getCustomerBroker().getPrintCustomerLst(request.getParameter("city"),request.getParameter("state"),request.getParameter("type"));
		request.setAttribute("custLst", custLst);
	}

	private void initializeGetCityList(HttpServletRequest request) {
		String[] cities = getCustomerBroker().getCityListForState(request.getParameter("txtState"));
		request.setAttribute(CITIES, cities);
		request.setAttribute("type", request.getParameter("sCustomerType"));
		request.setAttribute("state", request.getParameter("txtState"));
		String[] states = getCustomerBroker().getStateList();
		request.setAttribute(STATES, states);
	}

	private void initializeLabel(HttpServletRequest request) {
//		String[] cities = getCustomerBroker().getCityList();
//		request.setAttribute(CITIES, cities);
		String[] states = getCustomerBroker().getStateList();
		request.setAttribute(STATES, states);
	}

	private void addCity(HttpServletRequest request) {
		String city = (request.getParameter("txtCity") != null) ? request.getParameter("txtCity").trim() : "";
		String district = (request.getParameter("txtDistrict") != null) ? request.getParameter("txtDistrict").trim() : "";
		String state = (request.getParameter("txtState") != null) ? request.getParameter("txtState").trim() : "";
		if(city.equals("") || state.equals("")){
			MessageModel msgs = new MessageModel();
			msgs.addNewMessage(new Message(ERROR, "City or State field cannot be blank"));
			request.setAttribute(MESSAGES, msgs);
			request.setAttribute("city", city);
			request.setAttribute("district", district);
			request.setAttribute("state", state);
		} else {
			MessageModel msgs = getCustomerBroker().addCity(city, district, state);
			request.setAttribute("city", city);
			request.setAttribute("district", district);
			request.setAttribute("state", state);
			request.setAttribute(MESSAGES, msgs);
		}
		return;
	}

	private void initialize(HttpServletRequest request) {
		String[] cities = getCustomerBroker().getCityList();
		request.setAttribute(CITIES, cities);
		String[] countries = getCustomerBroker().getCountryList();
		request.setAttribute(COUNTRIES, countries);
		CustomerGroupModel[] customerGrps = getCustomerGroupBroker().getCustGroups();
		request.setAttribute(CUSTOMERS_GROUPS, customerGrps);
		int count = getCustomerBroker().getCustomerCount();
		request.setAttribute("count", count);
		int uncheck = getCustomerBroker().getUncheckedCustomerCount();
		request.setAttribute("uncheck", uncheck);
		String[] custTypes = getCustomerBroker().getCustomerTypes();
		request.setAttribute(CUST_TYPE, custTypes);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		return;
	}

	private void updateCustomer(HttpServletRequest request) {
		CustomerBroker broker = getCustomerBroker();
		CustomerModel model = populateDataModel(request);
		MessageModel msgModel = broker.updateCustomer(model);
		request.setAttribute(MESSAGES, msgModel);
		model = broker.getCustomerDtls(model.getCustomerId());
		request.setAttribute(FORM_DATA, model);
		initializeModify(request);
		return;
	}

	private void getCustomer(HttpServletRequest request) {
		CustomerBroker broker = getCustomerBroker();
		CustomerModel model = broker.getCustomerDtls(Integer.parseInt(request.getParameter("sCustomer")));
		request.setAttribute(FORM_DATA, model);
		initializeModify(request);
		return;
	}

	private void initializeModify(HttpServletRequest request) {
		initialize(request);
		return;
	}

	private void addCustomer(HttpServletRequest request,
			HttpServletResponse response) {
		CustomerModel model = populateDataModel(request);
//		String checked = request.getParameter("isChecked");
		MessageModel chkMsg = new MessageModel();
		if(chkMsg.getMessages().size() == 0){
			MessageModel msgModel = getCustomerBroker().addCustomer(model);
			request.setAttribute(MESSAGES, msgModel);
			if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
				request.setAttribute(FORM_DATA, model);
		} else {
			request.setAttribute(MESSAGES, chkMsg);
			request.setAttribute(FORM_DATA, model);
			request.setAttribute("checked","Y");
		}
		initialize(request);
		return;
	}

	private CustomerModel populateDataModel(HttpServletRequest request) {
		CustomerModel model = new CustomerModel();
		if(request.getParameter("sCustomerGrp") != null && !request.getParameter("sCustomerGrp").trim().equals("--"))
			model.setCustGroupId(Integer.parseInt(request.getParameter("sCustomerGrp")));
		if(request.getParameter("txtCustomerName") != null && !request.getParameter("txtCustomerName").trim().equals(""))
			model.setCustomerName(request.getParameter("txtCustomerName"));
		if(request.getParameter("txtContactPerson") != null && !request.getParameter("txtContactPerson").trim().equals(""))
			model.setContactPerson(request.getParameter("txtContactPerson"));
		if(request.getParameter("sCustomerType") != null && !request.getParameter("sCustomerType").trim().equals(""))
			model.setCustomerType(request.getParameter("sCustomerType"));
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
		if(request.getParameter("sGrade") != null && !request.getParameter("sGrade").trim().equals(""))
			model.setGrade(request.getParameter("sGrade"));
		if(request.getParameter("txtEmail") != null && !request.getParameter("txtEmail").trim().equals(""))
			model.setEmail(request.getParameter("txtEmail"));
		if(request.getParameter("txtEmail1") != null && !request.getParameter("txtEmail1").trim().equals(""))
			model.setEmail1(request.getParameter("txtEmail1"));
		if(request.getParameter("txtEmail2") != null && !request.getParameter("txtEmail2").trim().equals(""))
			model.setEmail2(request.getParameter("txtEmail2"));
		if(request.getParameter("txtEmail3") != null && !request.getParameter("txtEmail3").trim().equals(""))
			model.setEmail3(request.getParameter("txtEmail3"));
		if(request.getParameter("txtEmail4") != null && !request.getParameter("txtEmail4").trim().equals(""))
			model.setEmail4(request.getParameter("txtEmail4"));
		if(request.getParameter("txtEmail5") != null && !request.getParameter("txtEmail5").trim().equals(""))
			model.setEmail5(request.getParameter("txtEmail5"));
		if(request.getParameter("txtWebsite") != null && !request.getParameter("txtWebsite").trim().equals(""))
			model.setWebsite(request.getParameter("txtWebsite"));
		if(request.getParameter("txtTin") != null && !request.getParameter("txtTin").trim().equals(""))
			model.setTin(request.getParameter("txtTin"));
		if(request.getParameter("txtCst") != null && !request.getParameter("txtCst").trim().equals(""))
			model.setCst(request.getParameter("txtCst"));
		if(request.getParameter("txtTransport") != null && !request.getParameter("txtTransport").trim().equals(""))
			model.setTransport(request.getParameter("txtTransport"));
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("taRemarks") != null && !request.getParameter("taRemarks").trim().equals(""))
			model.setRemark(request.getParameter("taRemarks"));
		if(request.getParameter("sUser") != null && !request.getParameter("sUser").trim().equals(""))
			model.getCollectionPerson().setUserId(request.getParameter("sUser"));
		if(request.getParameter("txtOpenBal") != null && !request.getParameter("txtOpenBal").trim().equals(""))
			model.setOpenBal(Double.parseDouble(request.getParameter("txtOpenBal")));
		if(request.getParameter("taOpenbalRemarks") != null && !request.getParameter("taOpenbalRemarks").trim().equals(""))
			model.setOpenBalRemark(request.getParameter("taOpenbalRemarks"));
		if(request.getParameter("txtArea") != null && !request.getParameter("txtArea").trim().equals(""))
			model.setArea(request.getParameter("txtArea"));
		if(request.getParameter("rSupplier") != null && !request.getParameter("rSupplier").trim().equals("") && request.getParameter("rSupplier").trim().equals("Y"))
			model.setSupplier(true);
		return model;
	}

	private String resendCustomerMail(HttpServletRequest request) {
		MessageModel msgs = new MessageModel();
		try{
			
			String emailNo = request.getParameter("txtRefNo");
			CustomerModel customer = new CustomerModel();
			int custId = 0;
			CustomerEmailModel map = getCustomerBroker().getEmailDtls(emailNo);
			if(map != null){
				custId = map.getCustomer().getCustomerId();
				customer = getCustomerBroker().getCustomerDtls(custId);
				map.setCustomer(customer);
				request.setAttribute(FORM_DATA, map);
			}
			
//			int refNo = getCustomerBroker().emailCustomer(custId, email1, email2, subject, message, fileName, mobile1, mobile2);
			
			if(map.isAttachment())
				msgs = EmailUtil.sendCustomerAttMail(map);
			else 
				msgs = EmailUtil.sendCustomerMail(map);
			
			String smsMobile = (!map.getCustomer().getMobile1().equals("")) ? map.getCustomer().getMobile1() : (!map.getCustomer().getMobile2().equals("")) ? map.getCustomer().getMobile2() : "";
			String smsEmail = (!map.getCustomer().getEmail().equals("")) ? map.getCustomer().getEmail() : (!map.getCustomer().getEmail1().equals("")) ? map.getCustomer().getEmail1() : "";
			
//			System.out.println(smsMobile+"::"+smsEmail);

			if(!smsMobile.equals("")){
				URL url; 
//				BufferedReader bf;
//				String inputLine;
//				System.out.println(AUTH_SMS_BASE_URL+"&To="+mobile1+"&Message="+URLEncoder.encode("Email is sent to your email id "+smsEmail+" regarding "+subject+". HESHST","UTF-8"));
				url = new URL(AUTH_SMS_BASE_URL+"&To="+smsMobile+"&Message="+URLEncoder.encode("Email is sent to your email id "+smsEmail+" regarding "+map.getSubject()+". HESHST","UTF-8"));
				new BufferedReader(new InputStreamReader(url.openStream()));
//				while((inputLine = bf.readLine()) != null)
//				System.out.println(inputLine);
			}
	        
//	        initialize(request);
			
//	        msgs.addNewMessage(new Message(SUCCESS, "Email sent successfully!!!"));
	        
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while sending email!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
        
		return "customerEmail.jsp";
	}

}

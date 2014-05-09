package com.en.handler;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.EmailUtil;

public class PromotionalMailHandler extends Basehandler {

	public static String getPageName() {
		return PROMOTIONAL_MAIL_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "promotionalMail.jsp";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(isMultipart){
        	pageType = sendMail(request);
        } else {
        	initialize(request);
        }
		return pageType;
	}

	private void initialize(HttpServletRequest request) {
		String[] custTypes = getCustomerBroker().getCustomerTypes();
		String[] cities = getCustomerBroker().getDistinctCityList();
		String[] states = getCustomerBroker().getStateList();
		request.setAttribute(CUST_TYPE, custTypes);
		request.setAttribute(CITIES, cities);
		request.setAttribute(STATES, states);
	}

	private String sendMail(HttpServletRequest request) {
		MessageModel msgs = new MessageModel();
		try{
			HashMap<String, String> reqMap = new HashMap<String, String>(0);
			String subject = "";
			long id = (long) (Math.random() * 100000) / 1;
			@SuppressWarnings("unchecked")
			List<FileItem> items = (List<FileItem>) request.getAttribute(MUTLIPART_DATA);
			Iterator<FileItem> itr = items.iterator();

			String imgDir = "webapps//PROMOTIONAL_IMAGES//";
//			String imgDir = "E:\\PROMOTIONAL_IMAGES\\";
			
			File dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			itr = items.iterator();
			File saveFile = new File(imgDir + "Test" + id + ".jpg");
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					reqMap.put(name, value);
					if(name.equals("txtSubject")){
						subject = value;
					}
				} else {
					item.write(saveFile);
				}
			}
			
			String[] emails = null;
			
			if(reqMap.get("sTo").equals("1")){
				emails = getPromotionalMailBroker().getEmails(1, reqMap.get("sCustomerType"));
			} else if(reqMap.get("sTo").equals("2")){
				emails = getPromotionalMailBroker().getEmails(2, reqMap.get("txtCity"));
			} else if(reqMap.get("sTo").equals("3")){
				emails = getPromotionalMailBroker().getEmails(3, reqMap.get("txtState"));
			} else if(reqMap.get("sTo").equals("0")){
				emails = getPromotionalMailBroker().getEmails(0, ""); 
			}
//			System.out.println("Test"+id+".jpg");
			if(emails.length > 0){
				EmailUtil.sendImgMail(emails, subject, "Test"+id+".jpg");
				msgs.addNewMessage(new Message(SUCCESS, "Email Sent successfully to "+emails.length+" ids!!!"));
			} else {
				msgs.addNewMessage(new Message(ALERT, "No emails present for this particular filter!!!"));
			}
	        
	        initialize(request);
	        
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while sending email!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
        
		return "promotionalMail.jsp";
	}

}

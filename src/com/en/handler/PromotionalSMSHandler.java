package com.en.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.Message;
import com.en.model.MessageModel;

public class PromotionalSMSHandler extends Basehandler {

	public static String getPageName() {
		return PROMOTIONAL_SMS_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "promotionalSMS.jsp";
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(SEND_SMS)){
        	pageType = sendSMS(request);
        } else {
        	initialize(request);
        }
		return pageType;
	}

	private String sendSMS(HttpServletRequest request) {
		
		MessageModel msgs = new MessageModel();
		String sTo = request.getParameter("sTo");
		String SMS_Message = request.getParameter("txtMessage");
		try{
			
			String[] mobiles = null;
			
			if(sTo.equals("1")){
				mobiles = getPromotionalMailBroker().getMobileNos(1, request.getParameter("sCustomerType"));
			} else if(sTo.equals("2")){
				mobiles = getPromotionalMailBroker().getMobileNos(2, request.getParameter("txtCity"));
			} else if(sTo.equals("3")){
				mobiles = getPromotionalMailBroker().getMobileNos(3, request.getParameter("txtState"));
			} else if(sTo.equals("0")){
				mobiles = getPromotionalMailBroker().getMobileNos(0, ""); 
			}
			
			//mobiles = new String[] {"9884320072","9841368579","8754561006"};
			
			URL url = null;
			@SuppressWarnings("unused")
			BufferedReader bf = null;
			String url_String = AUTH_SMS_PRO_URL+"&Message="+SMS_Message+"&To=";
	        if(mobiles.length > 0){
	        	url_String = AUTH_SMS_PRO_URL+"&Message="+URLEncoder.encode(SMS_Message, "ISO-8859-1")+"&To=";
	        	for(int i=0; i<mobiles.length; i++){
	        		if(mobiles[i].length()==10)
					url_String = url_String+mobiles[i]+",";
					if(i!=0 && i%100 == 0){
						url = new URL(url_String.substring(0,url_String.length()-1));
						System.out.println(url);
						bf = new BufferedReader(new InputStreamReader(url.openStream()));
						String inputLine;
						while((inputLine = bf.readLine()) != null)
						System.out.println(inputLine);
			        	url_String = AUTH_SMS_PRO_URL+"&Message="+URLEncoder.encode(SMS_Message, "ISO-8859-1")+"&To=";
					}
	        	}
				url = new URL(url_String.substring(0,url_String.length()-1));
				bf = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine;
				while((inputLine = bf.readLine()) != null)
				System.out.println(inputLine);
	        }
			
	        initialize(request);
	        
	        msgs.addNewMessage(new Message(SUCCESS, mobiles.length+" SMS(es) sent successfully."));
	        
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while sending email!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
        
		return "promotionalSMS.jsp";
	}

	private void initialize(HttpServletRequest request) {
		String[] custTypes = getCustomerBroker().getCustomerTypes();
		String[] cities = getCustomerBroker().getDistinctCityList();
		String[] states = getCustomerBroker().getStateList();
		request.setAttribute(CUST_TYPE, custTypes);
		request.setAttribute(CITIES, cities);
		request.setAttribute(STATES, states); 
	}

} 

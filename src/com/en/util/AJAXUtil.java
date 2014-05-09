package com.en.util;

public class AJAXUtil implements Constant{
	
	public static String buildXmlAJAXMessage(String status, String message){
		StringBuffer xmlAjaxMessage = new StringBuffer(100);
		xmlAjaxMessage.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
		xmlAjaxMessage.append("<RESULT>");
		xmlAjaxMessage.append("<TYPE>"+status+"</TYPE>");
		xmlAjaxMessage.append("<MSG>"+message+"</MSG>");
		xmlAjaxMessage.append("</RESULT>");
		return xmlAjaxMessage.toString();
	}

}

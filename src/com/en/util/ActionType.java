package com.en.util;

public interface ActionType extends Constant {

	final static public String APPOINTMENT = "1";
	final static public String FORWARD = "2";
	final static public String QUOTATION = "3";
	final static public String APPOINTMENT_REPLY = "4";
	final static public String CLOSED = "0";
	final static public String CLOSED_APPROVAL = "7";
	final static public String ENQUIRY_ALERT = "5";
	final static public String APPOINTMENT_DELAY = "6";
	final static public String APPOINTMENT_CANCEL = "8";
	
	final static public String[] inquiry_actions = {
		"CLOSED","APPOINTMENT","FORWARD","QUOTATION","APPOINTMENT_REPLY","ENQUIRY_ALERT","APPOINTMENT_DELAY","CLOSED_APPROVAL","APPOINTMENT_CANCEL"
	};
	
	final static public String[] inquiry_actions_bg = {
		"black","pink","lime","aqua","yellow","#ACFA58","#FF0040","#424242","#FFE4B5"
	};
	
	final static public String[] inquiry_actions_color = {
		"white","red","maroon","blue","green","#0431B4","#BFFF00","#FACC2E","#DB2645"
	};
	
}

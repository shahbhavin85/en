package com.en.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil implements Constant {
	
	public static String getCurrDt(){
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dtFormat.format((new GregorianCalendar()).getTime());
	}
	
	public static String getCurrYear(){
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dtFormat.format((new GregorianCalendar()).getTime()).substring(6);
	}
	
	public static String getCurrMonth(){
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dtFormat.format((new GregorianCalendar()).getTime()).substring(3,5);
	}
	
	public static String getFirstDt(){
		String returnDt = "";
		String currDt = getCurrDt();
		int month = Integer.parseInt(currDt.substring(3,5));
		if(month < 4){
			returnDt = "01-04-"+(Integer.parseInt(currDt.substring(6))-1);
		} else {
			returnDt = "01-04-"+currDt.substring(6);
		}
		return returnDt;
	}
	
	public static Date addDays(Date date, long days){
		date.setTime(date.getTime()+(days*1000*60*60*24));
		return date;
	}
	
	public static String[] getCurrentQuarterDate(Calendar c){
		String[] dates = new String[2];
		int month = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		if(month<4){
			dates[0] = year+"-01-01";
			dates[1] = year+"-03-31";
		} else if(month<7){
			dates[0] = year+"-04-01";
			dates[1] = year+"-06-30";
		} else if(month<10){
			dates[0] = year+"-07-01";
			dates[1] = year+"-09-30";
		} else {
			dates[0] = year+"-10-01";
			dates[1] = year+"-12-31";
		} 
		return dates;
	}
	
}

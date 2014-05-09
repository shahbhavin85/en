package com.en.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.CustomerModel;

public class Utils implements Constant{
	
	public static String editSalesPass = getRandomPassword();

	public static double roundToDecimals(double d) {
		double temp=(double)((d*Math.pow(10,2)));
		return get2DecimalDouble(((double)temp)/Math.pow(10,2));
	}
	
	public static String get2Decimal(double d){
		BigDecimal roundfinalPrice = new BigDecimal(d).setScale(2,BigDecimal.ROUND_HALF_UP);
		Double doublePrice= new Double(roundfinalPrice.doubleValue()); 
		DecimalFormat formatter = new DecimalFormat("0.00");
		return formatter.format(doublePrice);
	}
	
	public static double get2DecimalDouble(double d){
		BigDecimal roundfinalPrice = new BigDecimal(d).setScale(2,BigDecimal.ROUND_HALF_UP);
		Double doublePrice= new Double(roundfinalPrice.doubleValue());
		return doublePrice;
	}
	
	public static String convertToSQLDate(String date){
		if(date.trim().equalsIgnoreCase("")){
			return "";
		}
		if(date.trim().indexOf("-") == 4)
			return date;
		return date.substring(6)+"-"+date.substring(3,5)+"-"+date.substring(0,2);
	}
	
	public static String convertToAppDate(String date){
		if(date.trim().equalsIgnoreCase("")){
			return "";
		}
		if(date.trim().indexOf("-") == 2)
			return date;
		return date.substring(8)+"-"+date.substring(5,7)+"-"+date.substring(0,4);
	}
	
	public static String convertToAppDateDDMMYY(String date){
		if(date.trim().equalsIgnoreCase("")){
			return "";
		}
		if(date.trim().indexOf("-") == 2)
			return date;
		return date.substring(8)+"/"+date.substring(5,7)+"/"+date.substring(2,4);
	}
	
	public static String convertToAppDateDDMM(String date){
		if(date.trim().equalsIgnoreCase("")){
			return "";
		}
		if(date.trim().indexOf("-") == 2)
			return date;
		return date.substring(8)+"/"+date.substring(5,7);
	}
	
	public static String convertToAppDateWithSlash(String date){
		if(date.trim().equalsIgnoreCase("")){
			return "";
		}
		if(date.trim().indexOf("-") == 2)
			return date;
		return date.substring(5,7)+"/"+date.substring(8)+"/"+date.substring(0,4);
	}
	
	public static String getAppTimeFormat(String time){
		if(time == null || time.equals("")){
			return "--";
		}
		if(Integer.parseInt(time.substring(0,2))>12){
			return (Integer.parseInt(time.substring(0,2))-12)+time.substring(2,5)+" pm";
		} else if(Integer.parseInt(time.substring(0,2))==12){
			return time.substring(0,5)+" pm";
		} else if(Integer.parseInt(time.substring(0,2))==0){
			return "12"+time.substring(2,5)+" am";
		} else {
			return time.substring(0,5)+" am";
		}
	}
	
	public static String padBillNo(int billno){
		String billNo = billno+"";
		for(int i=0; i<5-(billno+"").length();i++){
			billNo = "0"+billNo;
		}
		return billNo;
	}
	
	public static String getRandomPassword(){
		int lengthOfName = 10;
	    String	 name = "";
	    /* randomly choosing a name*/
	    for (int j = 0; j < lengthOfName; j++) {
	        int freq = (int)(Math.random() * 100) + 1;
	        if(freq <= 6){
	            name += "a";
	        }if(freq == 7 && freq == 8){
	            name += "b";
	        }if(freq >= 9 && freq <= 11){
	            name += "c";
	        }if(freq >= 12 && freq <= 15){
	            name += "d";
	        }if(freq >= 16 && freq <= 25){
	            name += "e";                        
	        }if(freq == 26 && freq == 27){
	            name += "f";
	        }if(freq == 28 && freq == 29){
	            name += "g";
	        }if(freq >= 30 && freq <= 33){
	            name += "h";
	        }if(freq >= 34 && freq <= 48){
	            name += "i";
	        }if(freq == 49 && freq == 50){
	            name += "j";
	        }if(freq >= 51 && freq <= 55){
	            name += "k";
	        }if(freq >= 56 && freq <= 60){
	            name += "l";
	        }if(freq == 61 && freq == 62){
	            name += "m";
	        }if(freq >= 63 && freq <= 70){
	            name += "n";
	        }if(freq >= 71 && freq <= 75){
	            name += "o";
	        }if(freq == 76 && freq == 77){
	            name += "p";
	        }if(freq == 78){
	            name += "q";
	        }if(freq >= 79 && freq <= 84){
	            name += "r";
	        }if(freq == 85 && freq == 86){
	            name += "s";
	        }if(freq == 87 && freq == 88){
	            name += "t";
	        }if(freq >= 89 && freq <= 93){
	            name += "u";
	        }if(freq == 94){
	            name += "v";
	        }if(freq == 95 && freq == 96){
	            name += "w";
	        }if(freq == 97){
	            name += "x";
	        }if(freq == 98 && freq == 99){
	            name += "y";
	        }if(freq == 100){
	            name += "z";
	        }
	    }
	    return name;
	}
	
	public static String getAdjustEntryType(int type){
		switch (type) {
		case 1:
			return "ORDER ADVANCE AGAINST SALES BILL"; 
		default:
			return "";
		}
	}
	
	public static String getEntryType(int type){
		switch (type){
			case 1 :
				return "Order Advance Cheque / DD Received";
			case 2 :
				return "Order Advcance Credit Card";
			case 3 :
				return "Order Advance Cash Received";
			case 4 :
				return "Order Advance Customer Direct Bank Deposit (Cash / Fund Transfer)";
			case 5 :
				return "Branch Cash Received";
			case 9 :
				return "Bank Cash Received";
			case 10 :
				return "Staff Advance Returned";
			case 11 :
				return "Other Charges Received";
			case 21 :
				return "Opening Balance Cash Recevied";
			case 22 :
				return "Opening Balance Credit Card";
			case 23 :
				return "Opening Balance Cheque / DD Received";
			case 24 :
				return "Opening Balance Customer Direct Bank Deposit (Cash / Fund Transfer)";
			case 31 :
				return "Cash Sales";
			case 32 :
				return "CC Sales";
			case 41 :
				return "Sales Bill Cash Recevied";
			case 42 :
				return "Sales Bill Credit Card";
			case 43 :
				return "Sales Bill Cheque / DD Received";
			case 44 :
				return "Sales Bill Customer Direct Bank Deposit (Cash / Fund Transfer)";
			case 46 :
				return "Labour Bill Cash Recevied";
			case 47 :
				return "Labour Bill Credit Card";
			case 48 :
				return "Labour Bill Cheque / DD Received";
			case 49 :
				return "Labour Bill Customer Direct Bank Deposit (Cash / Fund Transfer)";
			case 51 :
				return "Office Expenses";
			case 52 :
				return "Transport / Coolie / Courier";
			case 53 :
				return "Travelling Expenses";
			case 54 :
				return "Bank Deposit";
			case 55 :
				return "Paid to Supplier (Local Purchase)";
			case 56 :
				return "Others Expenses";
			case 57 :
				return "Printing / Stationary / Packing";
			case 58 :
				return "Salary Advance to Staff";
			case 59 :
				return "Salary to Staff";
			case 60 :
				return "Branch Cash Paid";
			case 61 :
				return "Commission";
			case 62 :
				return "Telephone Expenses";
			case 63 :
				return "Electricity Expenses";
			case 64 :
				return "Promotion / Advertising Expenses";
			case 65 :
				return "Wages";
			case 71 :
				return "Purchase Bill Cash Paid";
			case 72 :
				return "Purchase Bill Cheque / DD Paid";
			case 73 :
				return "Purchase Fund Transfer / RTGS / NEFT";
			case 81 :
				return "Journal Entry";
			case 82 :
				return "Assets";
			case 83 :
				return "FOREX";
			case 84 :
				return "Rent";
			case 85 :
				return "Income Tax";
			case 86 :
				return "Sales Tax";
			case 87 :
				return "TDS";
			case 88 :
				return "Insurance";
			case 89 :
				return "Bank Charges";
			default :
				return "";
		}			
	}
	
	public static HashMap<String, CustomerModel[]> reorderCustomer(CustomerModel[] customers){
		HashMap<String, CustomerModel[]> map = new HashMap<String, CustomerModel[]>(0);
		String[] states = RequestUtil.States;
		CustomerModel tempModel = null;
		ArrayList<CustomerModel> temp1 = new ArrayList<CustomerModel>(0);
		ArrayList<CustomerModel> temp = null;
		ArrayList<CustomerModel> tk = null;
		Iterator<CustomerModel> itr = null;
		for(int l=0; l<customers.length; l++){
			temp1.add(customers[l]);
		}
		String tempState = "";
		for(int i=0;i<states.length;i++){
			tempState = states[i];
			tk = new ArrayList<CustomerModel>(0);
			temp = null;
			temp = temp1;
			temp1 = new ArrayList<CustomerModel>(0);
			itr = temp.iterator();
			while(itr.hasNext()){
				tempModel = itr.next();
				if(tempModel.getState().equals(tempState)){
					tk.add(tempModel);
				} else {
					temp1.add(tempModel);
				}
			}
			if(tk.size()>0){
				map.put(states[i], (CustomerModel[])tk.toArray(new CustomerModel[0]));
			}
		}
		return map;
	}
	
	public static void copyFile(File sourceFile, File destFile){

	    FileChannel source = null;
	    FileChannel destination = null;
	    try {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();

	        // previous code: destination.transferFrom(source, 0, source.size());
	        // to avoid infinite loops, should be:
	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	try{
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
	    	} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	public static String convertString(String address){
		char alpha = '\n'; // µ
		int i = 0;
		while(address.indexOf(alpha, i) != -1){
			address = address.substring(0,address.indexOf(alpha, i))+"<br>"+address.substring(address.indexOf(alpha, i)+1);
			i = address.indexOf(alpha, i);
		}
		return address;
	}
	
	public static String convertStringWithSpace(String address){
		char alpha = '\n'; // µ
		int i = 0;
		while(address.indexOf(alpha, i) != -1){
			address = address.substring(0,address.indexOf(alpha, i))+" "+address.substring(address.indexOf(alpha, i)+1);
			i = address.indexOf(alpha, i);
		}
		return address;
	}
	
}

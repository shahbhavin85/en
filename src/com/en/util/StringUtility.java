package com.en.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class StringUtility {

	public static String JavaArray2JSArray(String[][] array){
		StringBuffer buff = new StringBuffer(100);
		buff.append("[");
		for(int i=0; i<array.length; i++){
			if(i!=0){
				buff.append(",");
			}
			buff.append("'"+StringUtility.replaceCots(array[i][1])+"'");
		}
		buff.append("]");
		return buff.toString();
	}
	
	public static String JavaArray2JSComboArray(String[][] array){
		StringBuffer buff = new StringBuffer(100);
		buff.append("[");
		for(int i=0; i<array.length; i++){
			if(i!=0){
				buff.append(",");
			}
			buff.append("['"+StringUtility.replaceCots(array[i][0])+"',");
			buff.append("'"+StringUtility.replaceCots(array[i][1])+"']");
		}
		buff.append("]");
		return buff.toString();
	}
	
	public static String buildItemCode(String Initials, long code){
		String itemcode = Initials;
		String sCode= code + "";
		int k = 5-sCode.length();
		for(int i=0; i<k ; i++){
			itemcode += "0";
		}
		itemcode += sCode;
		return itemcode;
	}
	
	public static String replace(String string, char a , String b){
	    final StringBuilder result = new StringBuilder();
	    final StringCharacterIterator iterator = new StringCharacterIterator(string);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	     
	      if (character == a) {
	         result.append(b);
	      }
	       else {
	        result.append(character);
	      }

	      
	      character = iterator.next();
	    }
	    return result.toString();
	  }
	
	public static String replaceCots(String string){
	    final StringBuilder result = new StringBuilder();
	    final StringCharacterIterator iterator = new StringCharacterIterator(string);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
			if (character == '\"') {
				result.append (" ");
			} else if (character == '\'') {
				result.append(" ");
			} else {
				result.append(character);
			}

	      character = iterator.next();
	    }
	    return result.toString();
	  }
	
	public static String buildAddress(String name, String address, String area, String city, String state, String pincode,
			String phone1, String phone2, String phone3, String phone4) {
		StringBuffer completeAddress = new StringBuffer(10);
		
		completeAddress.append("<B>"+name+"</B><BR>");
		if(address != null && !address.equals("")){
			completeAddress.append(address+" ");
		}
		if(area != null && !area.equals("")){
			completeAddress.append(area+"<BR>");
		} else {
			completeAddress.append("<BR>");					
		}
		if(city != null && !city.equals("")){
			completeAddress.append(city);
		}
		if(pincode != null && !pincode.equals("")){
			completeAddress.append("-"+pincode+" ");
		} else {
			completeAddress.append(" ");			
		}
		if(state != null && !state.equals("")){
			completeAddress.append(state+"<BR>");
		} else {
			completeAddress.append("<BR>");					
		}
		if((phone1!=null && !phone1.equals("")) || (phone2!=null && !phone2.equals(""))
				|| (phone3!=null && !phone3.equals("")) || (phone4!=null && !phone4.equals(""))){
			completeAddress.append("Phone : ");
			if(phone1!=null && !phone1.equals("")){
				completeAddress.append(phone1 + "/");
			}
			if(phone2!=null && !phone2.equals("")){
				completeAddress.append(phone2 + "/");
			}
			if(phone3!=null && !phone3.equals("")){
				completeAddress.append(phone3 + "/");
			}
			if(phone4!=null && !phone4.equals("")){
				completeAddress.append(phone4 + "/");
			}
			completeAddress.deleteCharAt(completeAddress.length()-1);
		} else {
			completeAddress.append("<BR>");					
		}
		
		return completeAddress.toString();
	}
	
	public static String buildAddressIn2Lines(String name, String address, String area, String city, String state, String pincode,
			String phone1, String phone2, String phone3, String phone4) {
		StringBuffer completeAddress = new StringBuffer(10);
		
		if(address != null && !address.equals("")){
			completeAddress.append(address+" ");
		}
		if(area != null && !area.equals("")){
			completeAddress.append(area+",");
		}
		if(city != null && !city.equals("")){
			completeAddress.append(city);
		}
		if(pincode != null && !pincode.equals("")){
			completeAddress.append("-"+pincode+" ");
		} else {
			completeAddress.append(" ");			
		}
		if(state != null && !state.equals("")){
			completeAddress.append(state+"<BR>");
		} else {
			completeAddress.append("<BR>");					
		}
		if((phone1!=null && !phone1.equals("")) || (phone2!=null && !phone2.equals(""))
				|| (phone3!=null && !phone3.equals("")) || (phone4!=null && !phone4.equals(""))){
			completeAddress.append("Phone : ");
			if(phone1!=null && !phone1.equals("")){
				completeAddress.append(phone1 + "/");
			}
			if(phone2!=null && !phone2.equals("")){
				completeAddress.append(phone2 + "/");
			}
			if(phone3!=null && !phone3.equals("")){
				completeAddress.append(phone3 + "/");
			}
			if(phone4!=null && !phone4.equals("")){
				completeAddress.append(phone4 + "/");
			}
			completeAddress.deleteCharAt(completeAddress.length()-1);
		} else {
			completeAddress.append("<BR>");					
		}
		
		return completeAddress.toString();
	}
	public static String convertToSQLAddress(String address){
		char alpha = '\u00B5'; // µ
		int i = 0;
		while(address.indexOf("\r\n", i) != -1){
			address = address.substring(0,address.indexOf("\r\n", i))+alpha+address.substring(address.indexOf("\r\n", i)+2);
			i = address.indexOf("\r\n", i);
		}
		return address;
	}
	
	public static String convertToAppAddress(String address){
		char alpha = '\u00B5'; // µ
		int i = 0;
		address = replaceCots(address);
		while(address.indexOf(alpha, i) != -1){
			address = address.substring(0,address.indexOf(alpha, i))+"\r\n"+address.substring(address.indexOf(alpha, i)+1);
			i = address.indexOf(alpha, i);
		}
		return address;
	}
	
	public static String printAddress(String address){
		char alpha = '\u00B5'; // µ
		int i = 0;
		while(address.indexOf(alpha, i) != -1){
			address = address.substring(0,address.indexOf(alpha, i))+"<br>"+address.substring(address.indexOf(alpha, i)+1);
			i = address.indexOf(alpha, i);
		}
		return address;
	}
}
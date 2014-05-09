package com.en.model;

import com.en.util.Constant;

public class Message implements Constant{

	private String type = "";
	private String msg = "";
	
	public Message(String type , String msg){
		this.type = type;
		this.msg = msg;
	}

	public String getType() {
		return type;
	}

	public String getMsg() {
		return msg;
	}

}

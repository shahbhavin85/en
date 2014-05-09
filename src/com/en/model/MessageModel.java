package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;
import com.en.model.Message;

public class MessageModel implements Constant {
	
	private ArrayList<Message> msgs = new ArrayList<Message>(0);
	
	public void addNewMessage(Message msg) {
		this.msgs.add(msg);
	}
	
	public ArrayList<Message> getMessages() {
		return this.msgs;
	}

}

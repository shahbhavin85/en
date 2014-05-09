package com.en.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import com.en.util.Constant;

public class ConversationModel implements Constant {

	private int conversationId = 0;
	private String subject = "";
	private UserModel owner = new UserModel();
	private Timestamp timeStamp = new Timestamp(0);
	private int status = 0;
	private ArrayList<UserModel> recipients = new ArrayList<UserModel>(0);
	private ArrayList<TaskModel> tasks = new ArrayList<TaskModel>(0);
	private int newCount = 0;
	private ArrayList<Integer> newMessages = new ArrayList<Integer>(0);
	public int getConversationId() {
		return conversationId;
	}
	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}
	public ArrayList<Integer> getNewMessages() {
		return newMessages;
	}
	public void setNewMessages(ArrayList<Integer> newMessages) {
		this.newMessages = newMessages;
	}
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public UserModel getOwner() {
		return owner;
	}
	public void setOwner(UserModel owner) {
		this.owner = owner;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public ArrayList<UserModel> getRecipients() {
		return recipients;
	}
	public void setRecipients(ArrayList<UserModel> recipients) {
		this.recipients = recipients;
	}
	public ArrayList<TaskModel> getTasks() {
		return tasks;
	}
	public void setTasks(ArrayList<TaskModel> tasks) {
		this.tasks = tasks;
	}
	public String getChildHtml(){
		StringBuffer childDetails = new StringBuffer(0);
		Iterator<TaskModel> itr = this.tasks.iterator();
		TaskModel temp = null;
		while(itr.hasNext()){
			temp = itr.next();
			try{
				childDetails.append(temp.getChildHtml());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "<ul id=\"tree\" style=\"margin-top:10px;\">"+childDetails.toString()+"</ul>";
	}
}

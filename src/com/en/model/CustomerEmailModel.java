package com.en.model;

import com.en.util.Constant;

public class CustomerEmailModel implements Constant {
	private int emailNo = 0;
	private String emailDate = "0000-00-00";
	private String subject = "";
	private String message = "";
	private String filename = "";
	private CustomerModel customer = new CustomerModel();
	public int getEmailNo() {
		return emailNo;
	}
	public void setEmailNo(int emailNo) {
		this.emailNo = emailNo;
	}
	public String getEmailDate() {
		return emailDate;
	}
	public void setEmailDate(String emailDate) {
		this.emailDate = emailDate;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public CustomerModel getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}
	public boolean isAttachment(){
		return !this.filename.equals("") ? true : false;
	}

}

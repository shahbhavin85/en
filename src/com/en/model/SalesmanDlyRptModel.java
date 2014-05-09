package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class SalesmanDlyRptModel implements Constant{

	private long rptId = 0;
	private UserModel user = new UserModel();
	private String rptDt = "";
	private String status = "";
	private String sentDt = "";
	private int orders = 0;
	private int quotation = 0;
	private UserModel approver = new UserModel();
	private ArrayList<SalesmanDlyItmModel> dtls = new ArrayList<SalesmanDlyItmModel>(0);
	private String remark = "";
	public long getRptId() {
		return rptId;
	}
	public void setRptId(long rptId) {
		this.rptId = rptId;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public String getRptDt() {
		return rptDt;
	}
	public void setRptDt(String rptDt) {
		this.rptDt = rptDt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<SalesmanDlyItmModel> getDtls() {
		return dtls;
	}
	public void addDtls(SalesmanDlyItmModel itm){
		this.dtls.add(itm);
	}
	public String getSentDt() {
		return sentDt;
	}
	public void setSentDt(String sentDt) {
		this.sentDt = sentDt;
	}
	public UserModel getApprover() {
		return approver;
	}
	public void setApprover(UserModel approver) {
		this.approver = approver;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public int getQuotation() {
		return quotation;
	}
	public void setQuotation(int quotation) {
		this.quotation = quotation;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

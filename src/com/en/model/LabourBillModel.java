package com.en.model;

import com.en.util.Constant;

public class LabourBillModel implements Constant {
	
	private int saleid = 0;
	private String salesdate = "";
	private CustomerModel customer = new CustomerModel();
	private double totalAmt = 0;
	private String remarks = "";
	private AccessPointModel branch = new AccessPointModel();
	private UserModel salesman = new UserModel();
	private String payDate = "";
	private double payAmt = 0;
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public double getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(double payAmt) {
		this.payAmt = payAmt;
	}
	public String getSalesdate() {
		return salesdate;
	}
	public void setSalesdate(String salesdate) {
		this.salesdate = salesdate;
	}
	public CustomerModel getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public AccessPointModel getBranch() {
		return branch;
	}
	public void setBranch(AccessPointModel branch) {
		this.branch = branch;
	}
	public UserModel getSalesman() {
		return salesman;
	}
	public void setSalesman(UserModel salesman) {
		this.salesman = salesman;
	}
}

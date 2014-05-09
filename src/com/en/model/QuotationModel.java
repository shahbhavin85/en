package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class QuotationModel implements Constant {
	
	private int quotationId = 0;
	private String quotationdate = "";
	private CustomerModel customer = new CustomerModel();
	private int taxtype = 0;
	private double totalAmt = 0;
	private String remarks = "";
	private AccessPointModel branch = new AccessPointModel();
	private UserModel salesman = new UserModel();
	private ArrayList<SalesItemModel> items = new ArrayList<SalesItemModel>();
	private boolean isLock = false;
	private double packing = 0;
	private double forwarding = 0;
	private double installation = 0;
	private double less = 0;
	private String validDate = "0000-00-00";
	private String followupDt = "";
	private String followupRemark = "";
	private int followupCnt = 0;
	private String followupUser = "";
	private String status = "0";
	private String orderNo = "";
	private int sample = 1;
	private int fromEx = 0;
	public int getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(int quotationId) {
		this.quotationId = quotationId;
	}
	public int getFromEx() {
		return fromEx;
	}
	public void setFromEx(int fromEx) {
		this.fromEx = fromEx;
	}
	public int getSample() {
		return sample;
	}
	public void setSample(int sample) {
		this.sample = sample;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFollowupDt() {
		return followupDt;
	}
	public void setFollowupDt(String followupDt) {
		this.followupDt = followupDt;
	}
	public String getFollowupRemark() {
		return followupRemark;
	}
	public void setFollowupRemark(String followupRemark) {
		this.followupRemark = followupRemark;
	}
	public int getFollowupCnt() {
		return followupCnt;
	}
	public void setFollowupCnt(int followupCnt) {
		this.followupCnt = followupCnt;
	}
	public String getFollowupUser() {
		return followupUser;
	}
	public void setFollowupUser(String followupUser) {
		this.followupUser = followupUser;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public double getPacking() {
		return packing;
	}
	public void setPacking(double packing) {
		this.packing = packing;
	}
	public double getForwarding() {
		return forwarding;
	}
	public void setForwarding(double forwarding) {
		this.forwarding = forwarding;
	}
	public double getInstallation() {
		return installation;
	}
	public void setInstallation(double installation) {
		this.installation = installation;
	}
	public double getLess() {
		return less;
	}
	public void setLess(double less) {
		this.less = less;
	}
	public boolean isLock() {
		return isLock;
	}
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	public String getQuotationdate() {
		return quotationdate;
	}
	public void setQuotationdate(String quotationdate) {
		this.quotationdate = quotationdate;
	}
	public CustomerModel getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}
	public int getTaxtype() {
		return taxtype;
	}
	public void setTaxtype(int taxtype) {
		this.taxtype = taxtype;
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
	public ArrayList<SalesItemModel> getItems() {
		return items;
	}
	public void setItems(ArrayList<SalesItemModel> items) {
		this.items = items;
	}
	public void addItem(ItemModel item, String desc, double qty, double rate, double disrate) {
		SalesItemModel model = new SalesItemModel();
		model.setItem(item);
		model.setQty(qty);
		model.setRate(rate);
		model.setDesc(desc);
		model.setDisrate(disrate);
		this.items.add(model);
	}
	public void addItem(ItemModel item, String desc, double qty, double rate, double taxrate, double disrate) {
		SalesItemModel model = new SalesItemModel();
		model.setItem(item);
		model.setQty(qty);
		model.setRate(rate);
		model.setDesc(desc);
		model.setTaxrate(taxrate);
		model.setDisrate(disrate);
		this.items.add(model);
	}
}

package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class OrderModel implements Constant {
	
	private int orderId = 0;
	private String orderDate = "";
	private CustomerModel customer = new CustomerModel();
	private int taxtype = 0;
	private double totalAmt = 0;
	private double sentAmt = 0;
	private String remarks = "";
	private AccessPointModel branch = new AccessPointModel();
	private UserModel salesman = new UserModel();
	private ArrayList<SalesItemModel> items = new ArrayList<SalesItemModel>();
	private boolean isLock = false;
	private double packing = 0;
	private double forwarding = 0;
	private double installation = 0;
	private double less = 0;
	private double advance = 0;
	private double usedAdvance = 0;
	private int priority = 0; 
	private int devDays = 0;
	private String formNo = "";
	private String devAddress = "";
	private int payType = 0;
	private int credDays = 0;
	private double downPay = 0;
	private int EMINo = 0;
	private double EMIAmt = 0;
	private int EMIDays = 0;
	private String followupDt = "";
	private String followupRemark = "";
	private int followupCnt = 0;
	private String followupUser = "";
	private int fromEx = 0;
	private boolean isEmail = false;
	private int status = 0;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getStatus() {
		return status;
	}
	public String getStatusString() {
		return (status == 0) ? "OPEN" : ((status == 1) ? "CANCELLED" : "COMPLETED");
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getSentAmt() {
		return sentAmt;
	}
	public void setSentAmt(double sentAmt) {
		this.sentAmt = sentAmt;
	}
	public double getUsedAdvance() {
		return usedAdvance;
	}
	public void setUsedAdvance(double usedAdvance) {
		this.usedAdvance = usedAdvance;
	}
	public boolean isEmail() {
		return isEmail;
	}
	public void setEmail() {
		this.isEmail = true;
	}
	public int getFromEx() {
		return fromEx;
	}
	public void setFromEx(int fromEx) {
		this.fromEx = fromEx;
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
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getDevDays() {
		return devDays;
	}
	public void setDevDays(int devDays) {
		this.devDays = devDays;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getDevAddress() {
		return devAddress;
	}
	public void setDevAddress(String devAddress) {
		this.devAddress = devAddress;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public int getCredDays() {
		return credDays;
	}
	public void setCredDays(int credDays) {
		this.credDays = credDays;
	}
	public double getDownPay() {
		return downPay;
	}
	public void setDownPay(double downPay) {
		this.downPay = downPay;
	}
	public int getEMINo() {
		return EMINo;
	}
	public void setEMINo(int eMINo) {
		EMINo = eMINo;
	}
	public double getEMIAmt() {
		return EMIAmt;
	}
	public void setEMIAmt(double eMIAmt) {
		EMIAmt = eMIAmt;
	}
	public int getEMIDays() {
		return EMIDays;
	}
	public void setEMIDays(int eMIDays) {
		EMIDays = eMIDays;
	}
	public double getAdvance() {
		return advance;
	}
	public void setAdvance(double advance) {
		this.advance = advance;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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
	public void addItem(ItemModel item, String desc, double qty, double rate, double taxrate, double disrate, double sentQty) {
		SalesItemModel model = new SalesItemModel();
		model.setItem(item);
		model.setQty(qty);
		model.setRate(rate);
		model.setDesc(desc);
		model.setTaxrate(taxrate);
		model.setDisrate(disrate);
		model.setSentQty(sentQty);
		this.items.add(model);
	}
}

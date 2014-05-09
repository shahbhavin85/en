package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;
import com.en.util.Utils;

public class SalesModel implements Constant {
	
	private int saleid = 0;
	private String salesdate = "";
	private CustomerModel customer = new CustomerModel();
	private int paymode = 0;
	private int taxtype = 0;
	private double packing = 0;
	private double forwarding = 0;
	private double installation = 0;
	private double less = 0;
	private double cess = 0;
	private double totalAmt = 0;
	private String transport = "";
	private String lrno = "";
	private String lrdt = "";
	private String remarks = "";
	private AccessPointModel branch = new AccessPointModel();
	private UserModel salesman = new UserModel();
	private UserModel packedBy = new UserModel();
	private UserModel despatchedBy = new UserModel();
	private ArrayList<SalesItemModel> items = new ArrayList<SalesItemModel>();
	private int creditDays = 0;
	private boolean isLock = false;
	private String dueDate = "";
	private String payDate = "";
	private double payAmt = 0;
	private double adjAmt = 0;
	private String followupDt = "";
	private String followupRemark = "";
	private int followupCnt = 0;
	private String followupUser = "";
	private int ctns = 0;
	private int status = 0;
	private int alert = 0;
	private double alertDis = 0;
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public double getAdjAmt() {
		return adjAmt;
	}
	public void setAdjAmt(double adjAmt) {
		this.adjAmt = adjAmt;
	}
	public int getAlert() {
		return alert;
	}
	public void setAlert(int alert) {
		this.alert = alert;
	}
	public double getAlertDis() {
		return alertDis;
	}
	public void setAlertDis(double alertDis) {
		this.alertDis = alertDis;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCtns() {
		return ctns;
	}
	public void setCtns(int ctns) {
		this.ctns = ctns;
	}
	public String getFollowupUser() {
		return followupUser;
	}
	public void setFollowupUser(String followupUser) {
		this.followupUser = followupUser;
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
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public int getCreditDays() {
		return creditDays;
	}
	public void setCreditDays(int creditDays) {
		this.creditDays = creditDays;
	}
	public boolean isLock() {
		return isLock;
	}
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getSalesdate() {
		return salesdate;
	}
	public String getPrintSalesdate() {
		return Utils.convertToAppDate(salesdate);
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
	public int getPaymode() {
		return paymode;
	}
	public void setPaymode(int paymode) {
		this.paymode = paymode;
	}
	public int getTaxtype() {
		return taxtype;
	}
	public void setTaxtype(int taxtype) {
		this.taxtype = taxtype;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public String getTransport() {
		return transport.toUpperCase();
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getLrno() {
		return lrno.toUpperCase();
	}
	public void setLrno(String lrno) {
		this.lrno = lrno;
	}
	public String getLrdt() {
		return lrdt.toUpperCase();
	}
	public void setLrdt(String lrdt) {
		this.lrdt = lrdt;
	}
	public double getCess() {
		return cess;
	}
	public void setCess(double cess) {
		this.cess = cess;
	}
	public UserModel getPackedBy() {
		return packedBy;
	}
	public void setPackedBy(UserModel packedBy) {
		this.packedBy = packedBy;
	}
	public UserModel getDespatchedBy() {
		return despatchedBy;
	}
	public void setDespatchedBy(UserModel despatchedBy) {
		this.despatchedBy = despatchedBy;
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

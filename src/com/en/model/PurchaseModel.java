package com.en.model;

import java.util.ArrayList;

public class PurchaseModel {

	private int purchaseId = 0;
	private CustomerModel supplier = new CustomerModel();
	private String invNo = "";
	private String invDt = "0000-00-00";
	private String recdDt = "0000-00-00";
	private String remark = "";
	private AccessPointModel branch = new AccessPointModel();
	private int billType = 0;
	private ArrayList<PurchaseItemModel> items = new ArrayList<PurchaseItemModel>(0);
	private double discount = 0;
	private double extra = 0;
	private double totalAmt = 0;
	private boolean lock = false;
	private String payDate = "";
	private double payAmt = 0;
	public int getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
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
	public boolean isLock() {
		return lock;
	}
	public void setLock(boolean lock) {
		this.lock = lock;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getExtra() {
		return extra;
	}
	public void setExtra(double extra) {
		this.extra = extra;
	}
	public CustomerModel getSupplier() {
		return supplier;
	}
	public void setSupplier(CustomerModel supplier) {
		this.supplier = supplier;
	}
	public String getInvNo() {
		return invNo;
	}
	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}
	public String getInvDt() {
		return invDt;
	}
	public void setInvDt(String invDt) {
		this.invDt = invDt;
	}
	public String getRecdDt() {
		return recdDt;
	}
	public void setRecdDt(String recdDt) {
		this.recdDt = recdDt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public AccessPointModel getBranch() {
		return branch;
	}
	public void setBranch(AccessPointModel branch) {
		this.branch = branch;
	}
	public int getBillType() {
		return billType;
	}
	public void setBillType(int billType) {
		this.billType = billType;
	}
	public ArrayList<PurchaseItemModel> getItems() {
		return items;
	}
	public void addItem(ItemModel item, double qty, double rate) {
		PurchaseItemModel model = new PurchaseItemModel();
		model.setItem(item);
		model.setQty(qty);
		model.setRate(rate);
		this.items.add(model);
	}
	public void addItem(ItemModel item, double qty, double rate, double taxrate) {
		PurchaseItemModel model = new PurchaseItemModel();
		model.setItem(item);
		model.setQty(qty);
		model.setRate(rate);
		model.setTax(taxrate);
		this.items.add(model);
	}
	
}

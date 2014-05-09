package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class PurchaseReturnModel implements Constant{

	private int purchaseId = 0;
	private CustomerModel supplier = new CustomerModel();
	private String returnDt = "0000-00-00";
	private String remark = "";
	private AccessPointModel branch = new AccessPointModel();
	private int billType = 0;
	private ArrayList<PurchaseItemModel> items = new ArrayList<PurchaseItemModel>(0);
	private double totalAmt = 0;
	private boolean isLock = false;
	public int getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	public boolean isLock() {
		return isLock;
	}
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	public String getReturnDt() {
		return returnDt;
	}
	public void setReturnDt(String returnDt) {
		this.returnDt = returnDt;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public CustomerModel getSupplier() {
		return supplier;
	}
	public void setSupplier(CustomerModel supplier) {
		this.supplier = supplier;
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

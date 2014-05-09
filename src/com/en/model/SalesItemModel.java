package com.en.model;

import com.en.util.Constant;

public class SalesItemModel implements Constant {

	private ItemModel item = new ItemModel();
	private double qty = 0;
	private double sentQty = 0;
	private double reqQty = 0;
	private double rate = 0;
	private double disrate = 0;
	private double taxrate = 0;
	private String salesId = "";
	private String billPrefix = "";
	private int branchId = 0;
	private String desc = "--";
	public ItemModel getItem() {
		return item;
	}
	public void setItem(ItemModel item) {
		this.item = item;
	}
	public double getReqQty() {
		return reqQty;
	}
	public void setReqQty(double reqQty) {
		this.reqQty = reqQty;
	}
	public double getSentQty() {
		return sentQty;
	}
	public void setSentQty(double sentQty) {
		this.sentQty = sentQty;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getDisrate() {
		return disrate;
	}
	public void setDisrate(double disrate) {
		this.disrate = disrate;
	}
	public double getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(double taxrate) {
		this.taxrate = taxrate;
	}
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getBillPrefix() {
		return billPrefix;
	}
	public void setBillPrefix(String billPrefix) {
		this.billPrefix = billPrefix;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc =  desc.equals("") ? "" : desc.toUpperCase();
	}
}

package com.en.model;

import com.en.util.Constant;

public class TransferItemModel implements Constant {

	private ItemModel item = new ItemModel();
	private double qty = 0;
	private double rate = 0;
	private double sentQty = 0;
	private String transferId = "";
	private String billPrefix = "";
	private String desc = "--";
	private int branchId = 0;
	public ItemModel getItem() {
		return item;
	}
	public void setItem(ItemModel item) {
		this.item = item;
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
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
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
		this.desc = desc;
	}

}

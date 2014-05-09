package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class TransferModel implements Constant {
	
	private int transferid = 0;
	private String transferdate = "";
	private AccessPointModel fromBranch = new AccessPointModel();
	private AccessPointModel toBranch = new AccessPointModel();
	private String transport = "";
	private String lrno = "";
	private String lrdt = "";
	private String remark = "";
	private double totalAmt = 0;
	private double totalQty = 0;
	private double sentQty = 0;
	private boolean isToApproved = false;
	private String toAppDate = "";
	private String appOnDate = "";
	private UserModel toApprover = new UserModel();
	private boolean lock = false;
	private String followupDt = "";
	private String followupRemark = "";
	private int followupCnt = 0;
	private String followupUser = "";
	private ArrayList<TransferItemModel> items = new ArrayList<TransferItemModel>();
	public int getTransferid() {
		return transferid;
	}
	public void setTransferid(int transferid) {
		this.transferid = transferid;
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
	public double getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(double totalQty) {
		this.totalQty = totalQty;
	}
	public double getSentQty() {
		return sentQty;
	}
	public void setSentQty(double sentQty) {
		this.sentQty = sentQty;
	}
	public boolean isLock() {
		return lock;
	}
	public void setLock(boolean lock) {
		this.lock = lock;
	}
	public String getAppOnDate() {
		return appOnDate;
	}
	public void setAppOnDate(String appOnDate) {
		this.appOnDate = appOnDate;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getTransferdate() {
		return transferdate;
	}
	public void setTransferdate(String transferdate) {
		this.transferdate = transferdate;
	}
	public AccessPointModel getFromBranch() {
		return fromBranch;
	}
	public void setFromBranch(AccessPointModel fromBranch) {
		this.fromBranch = fromBranch;
	}
	public AccessPointModel getToBranch() {
		return toBranch;
	}
	public void setToBranch(AccessPointModel toBranch) {
		this.toBranch = toBranch;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getLrno() {
		return lrno;
	}
	public void setLrno(String lrno) {
		this.lrno = lrno;
	}
	public String getLrdt() {
		return lrdt;
	}
	public void setLrdt(String lrdt) {
		this.lrdt = lrdt;
	}
	public ArrayList<TransferItemModel> getItems() {
		return items;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public boolean isToApproved() {
		return isToApproved;
	}
	public void setToApproved() {
		this.isToApproved = true;
	}
	public String getToAppDate() {
		return toAppDate;
	}
	public void setToAppDate(String toAppDate) {
		this.toAppDate = toAppDate;
	}
	public UserModel getToApprover() {
		return toApprover;
	}
	public void setToApprover(UserModel toApprover) {
		this.toApprover = toApprover;
	}
	public void addItem(ItemModel item, String desc, double qty) {
		TransferItemModel model = new TransferItemModel();
		model.setItem(item);
		model.setQty(qty);
		model.setDesc(desc);
		this.items.add(model);
	}
	public void addItem(ItemModel item, String desc, double qty, double rate) {
		TransferItemModel model = new TransferItemModel();
		model.setItem(item);
		model.setQty(qty);
		model.setRate(rate);
		model.setDesc(desc);
		this.items.add(model);
	}

}

package com.en.model;

import com.en.util.Constant;

public class AttendanceModel implements Constant {

	private UserModel user = new UserModel();
	private String type = "";
	private String ot = "";
	private String inTime = "";
	private String outTime = "";
	private String lateFine = "";
	private String panelty = "";
	private String remarks = "";
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOt() {
		return ot;
	}
	public void setOt(String ot) {
		this.ot = ot;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public String getLateFine() {
		return lateFine;
	}
	public void setLateFine(String lateFine) {
		this.lateFine = lateFine;
	}
	public String getPanelty() {
		return panelty;
	}
	public void setPanelty(String panelty) {
		this.panelty = panelty;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}

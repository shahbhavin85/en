package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class DayBookEntryModel implements Constant {
	
	private long id = 0;
	private String entryDate = "0000-00-00";
	private String previousDate = "0000-00-00";
	private String nextDate = "0000-00-00";
	private AccessPointModel branch = new AccessPointModel();
	private String status = "N";
	private double openingBal = 0;
	private UserModel approver = new UserModel();
	private ArrayList<EntryModel> entries = new ArrayList<EntryModel>(0);
	public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPreviousDate() {
		return previousDate;
	}
	public void setPreviousDate(String previousDate) {
		this.previousDate = previousDate;
	}
	public String getNextDate() {
		return nextDate;
	}
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	public double getOpeningBal() {
		return openingBal;
	}
	public void setOpeningBal(double openingBal) {
		this.openingBal = openingBal;
	}
	public String getStatus() {
		return status;
	}
	public String getDisplayStatus() {
		return (status.equals("N")) ? "Open" : (status.equals("Y")) ? "Approved" : (status.equals("R")) ? "Rejected" : "Send for approval";
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UserModel getApprover() {
		return approver;
	}
	public void setApprover(UserModel approver) {
		this.approver = approver;
	}
	public void setEntries(ArrayList<EntryModel> entries) {
		this.entries = entries;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public AccessPointModel getBranch() {
		return branch;
	}
	public void setBranch(AccessPointModel branch) {
		this.branch = branch;
	}
	public EntryModel[] getEntries() {
		return (EntryModel[])entries.toArray(new EntryModel[0]);
	}
	public void addEntry(EntryModel entry){
		this.entries.add(entry);
	}
}

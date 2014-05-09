package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class BranchStockEntryModel implements Constant {

	private UserModel checkedBy = new UserModel();
	private int day = 0;
	private int month = 0;
	private int year = 0;
	private AccessPointModel branch = new AccessPointModel();
	private ArrayList<BranchStockEntryItemModel> items = new ArrayList<BranchStockEntryItemModel>(0);
	public UserModel getCheckedBy() {
		return checkedBy;
	}
	public void setCheckedBy(UserModel checkedBy) {
		this.checkedBy = checkedBy;
	}
	public AccessPointModel getBranch() {
		return branch;
	}
	public void setBranch(AccessPointModel branch) {
		this.branch = branch;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public ArrayList<BranchStockEntryItemModel> getItems() {
		return items;
	}
	public void setItems(ArrayList<BranchStockEntryItemModel> items) {
		this.items = items;
	}
	public void addItem(BranchStockEntryItemModel item){
		this.items.add(item);
	}
	public String getSQLDate(){
		return this.year+"-"+(this.month < 10  ? "0"+this.month : this.month)+"-"+(this.day < 10 ? "0"+this.day : this.day); 
	}
}

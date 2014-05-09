package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;
import com.en.util.Utils;

public class CreditNoteModel implements Constant {
	
	private int saleid = 0;
	private String salesdate = "";
	private CustomerModel customer = new CustomerModel();
	private int taxtype = 0;
	private double less = 0;
	private double totalAmt = 0;
	private String remarks = "";
	private AccessPointModel branch = new AccessPointModel();
	private UserModel salesman = new UserModel();
	private ArrayList<SalesItemModel> items = new ArrayList<SalesItemModel>();
	private boolean isLock = false;
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
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
	public double getLess() {
		return less;
	}
	public void setLess(double less) {
		this.less = less;
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

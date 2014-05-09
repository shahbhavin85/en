package com.en.model;

import com.en.util.Constant;
import com.en.util.Utils;

public class SalesmanDlyItmModel implements Constant{

	private String inTime;
	private String outTime;
	private String remark;
	private String type;
	private long index;
	private String editable = "Y";
	private CustomerModel customer = new CustomerModel();
	private double lodging = 0;
	private double food = 0;
	private double railbus = 0;
	private double localtransport = 0;
	private double courier = 0;
	private double others = 0;
	public String getInTime() {
		return inTime;
	}
	public String getAppInTime() {
		return Utils.getAppTimeFormat(this.inTime);
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public String getAppOutTime() {
		return Utils.getAppTimeFormat(this.outTime);
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public CustomerModel getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}
	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}
	public double getLodging() {
		return lodging;
	}
	public void setLodging(double lodging) {
		this.lodging = lodging;
	}
	public double getFood() {
		return food;
	}
	public void setFood(double food) {
		this.food = food;
	}
	public double getRailbus() {
		return railbus;
	}
	public void setRailbus(double railbus) {
		this.railbus = railbus;
	}
	public double getLocaltransport() {
		return localtransport;
	}
	public void setLocaltransport(double localtransport) {
		this.localtransport = localtransport;
	}
	public double getCourier() {
		return courier;
	}
	public void setCourier(double courier) {
		this.courier = courier;
	}
	public double getOthers() {
		return others;
	}
	public void setOthers(double others) {
		this.others = others;
	}
}

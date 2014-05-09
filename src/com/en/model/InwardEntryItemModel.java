package com.en.model;

import com.en.util.Constant;

public class InwardEntryItemModel implements Constant {

	private AdminItemModel item = new AdminItemModel();
	private double qty = 0;
	private double rate = 0;
	private double cbm = 0;
	private double duty = 0;
	public AdminItemModel getItem() {
		return item;
	}
	public void setItem(AdminItemModel item) {
		this.item = item;
	}
	public double getDuty() {
		return duty;
	}
	public void setDuty(double duty) {
		this.duty = duty;
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
	public double getCbm() {
		return cbm;
	}
	public void setCbm(double cbm) {
		this.cbm = cbm;
	}
}

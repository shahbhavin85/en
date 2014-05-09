package com.en.model;

public class StockItemModel extends ItemModel {

	private double openQty = 0;
	private double plusQty = 0;
	private double minusQty = 0;
	private String date = "";
	private String desc = "";
	public double getOpenQty() {
		return openQty;
	}
	public void setOpenQty(double openQty) {
		this.openQty = openQty;
	}
	public double getPlusQty() {
		return plusQty;
	}
	public void setPlusQty(double plusQty) {
		this.plusQty = plusQty;
	}
	public double getMinusQty() {
		return minusQty;
	}
	public void setMinusQty(double minusQty) {
		this.minusQty = minusQty;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}

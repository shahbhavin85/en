package com.en.model;

import com.en.util.Utils;

public class ItemModel extends ItemCategoryModel {
	
	private int itemId = 0;
	private String itemName = "";
	private String itemNumber = "";
	private String itemLabel = "";
	private String itemPrice = "";
	private String itemWPrice = "";
	private String offerPrice = "0";
	private String offerDate ="0000-00-00";
	private String devTime = "0";
	private String tax = "1";
	private String discount = "";
	private String alterDate ="0000-00-00";
	private String fromDate ="0000-00-00";
	private String toDate ="0000-00-00";
	private boolean inPL = true;
	private boolean inSales = true;
	private boolean isCatalogue = false;
	private boolean isPhoto = false;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getAlterDate() {
		return alterDate;
	}
	public void setAlterDate(String alterDate) {
		this.alterDate = alterDate;
	}
	public boolean isCatalogue() {
		return isCatalogue;
	}
	public void setCatalogue() {
		this.isCatalogue = true;
	}
	public boolean isPhoto() {
		return isPhoto;
	}
	public void setPhoto() {
		this.isPhoto = true;
	}
	public boolean isInPL() {
		return inPL;
	}
	public void setInPL() {
		this.inPL = false;
	}
	public boolean isInSales() {
		return inSales;
	}
	public void setInSales() {
		this.inSales = false;
	}
	public String getItemWPrice() {
		return itemWPrice;
	}
	public void setItemWPrice(String itemWPrice) {
		this.itemWPrice = itemWPrice;
	}
	public String getFromDate() {
		return fromDate;
	}
	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getItemName() {
		return itemName.toUpperCase();
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemNumber() {
		return itemNumber.toUpperCase();
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getItemPrice() {
		return itemPrice.toUpperCase();
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getOfferPrice() {
		return offerPrice.toUpperCase();
	}
	public void setOfferPrice(String offerPrice) {
		if(!offerPrice.trim().equals(""))
			this.offerPrice = offerPrice;
	}
	public String getAppOfferDate() {
		if(!offerDate.trim().equals("") && !offerDate.trim().equals("0000-00-00"))
			return Utils.convertToAppDate(offerDate);
		return offerDate.toUpperCase();
	}
	public String getSQLOfferDate() {
		if(!offerDate.trim().equals(""))
			return Utils.convertToSQLDate(offerDate);
		return offerDate.toUpperCase();
	}
	public void setOfferDate(String offerDate) {
		if(!offerDate.trim().equals("") && !offerDate.trim().equals("0000-00-00"))
			this.offerDate = offerDate.toUpperCase();
	}
	public String getDevTime() {
		return devTime.toUpperCase();
	}
	public void setDevTime(String devTime) {
		if(!devTime.trim().equals(""))
			this.devTime = devTime;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
}

package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class InwardEntryModel implements Constant {
	
	private int entryNo = 0;
	private String BENo = "";
	private String remarks = "";
	private double cbm = 0;
	private double sourceExp = 0;
	private double destinationExp = 0;
	private double exchangeRate = 0;
	private String entryDate = "0000-00-00";
	private ArrayList<InwardEntryItemModel> items = new ArrayList<InwardEntryItemModel>(0);
	public int getEntryNo() {
		return entryNo;
	}
	public void setEntryNo(int entryNo) {
		this.entryNo = entryNo;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public InwardEntryItemModel[] getItems() {
		return (InwardEntryItemModel[])items.toArray(new InwardEntryItemModel[0]);
	}
	public void addItem(InwardEntryItemModel item){
		this.items.add(item);
	}
	public String getBENo() {
		return BENo;
	}
	public void setBENo(String bENo) {
		BENo = bENo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public double getCbm() {
		return cbm;
	}
	public void setCbm(double cbm) {
		this.cbm = cbm;
	}
	public double getSourceExp() {
		return sourceExp;
	}
	public void setSourceExp(double sourceExp) {
		this.sourceExp = sourceExp;
	}
	public double getDestinationExp() {
		return destinationExp;
	}
	public void setDestinationExp(double destinationExp) {
		this.destinationExp = destinationExp;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}

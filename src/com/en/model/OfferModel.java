package com.en.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.en.util.Utils;


public class OfferModel {

	
	private int offerid = 0;
	private String offerName = "";
	private String offerPrice = "0";
	private String fromDate = "0000-00-00";
	private String toDate = "0000-00-00";
	private double packing = 0;
	private double forwarding = 0;
	private double installation = 0;
	private ArrayList<OfferItemModel> offerItems = new ArrayList<OfferItemModel>(0);
	
	public int getOfferid() {
		return offerid;
	}
	public void setOfferid(int offerid) {
		this.offerid = offerid;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public double getPacking() {
		return packing;
	}
	public void setPacking(double packing) {
		this.packing = packing;
	}
	public double getForwarding() {
		return forwarding;
	}
	public void setForwarding(double forwarding) {
		this.forwarding = forwarding;
	}
	public double getInstallation() {
		return installation;
	}
	public void setInstallation(double installation) {
		this.installation = installation;
	}
	public String getOfferPrice() {
		return offerPrice;
	}
	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}
	public String getFromDate() {
		return fromDate;
	}
	public String getAppFromDate() {
		if(!fromDate.trim().equals("") && !fromDate.trim().equals("0000-00-00"))
			return Utils.convertToAppDate(fromDate);
		return fromDate;
	}
	public String getSQLFromDate() {
		if(!fromDate.trim().equals(""))
			return Utils.convertToSQLDate(fromDate);
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public String getAppToDate() {
		if(!toDate.trim().equals("") && !toDate.trim().equals("0000-00-00"))
			return Utils.convertToAppDate(toDate);
		return toDate;
	}
	public String getSQLToDate() {
		if(!toDate.trim().equals(""))
			return Utils.convertToSQLDate(toDate);
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public ArrayList<OfferItemModel> getOfferItems() {
		return offerItems;
	}
	public void addOfferItem(ItemModel item, int qty){
		this.offerItems.add(new OfferItemModel(item,qty));
	}
	public String getOfferDesc(){
		StringBuffer offerDesc = new StringBuffer(0);
		if(this.getOfferItems().size()>0){
			Iterator<OfferItemModel> offerItms = this.getOfferItems().iterator();
			int i =1;
			double total = 0;
			StringBuffer tmp = new StringBuffer(0);
			while(offerItms.hasNext()){
				OfferItemModel item = offerItms.next();
				tmp.append(i+". "+item.getQty()+" Nos of "+item.getItem().getItemNumber()+"<BR>");
				total = total + (item.getQty()*(Double.parseDouble(item.getItem().getItemPrice())));
				i++;
			}
			offerDesc.append(tmp);
		}
		return offerDesc.toString();
	}
}

package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class EnquiryModel implements Constant {
	
	private CustomerModel customer = new CustomerModel();
	private long enquiryNo;
	private String priority;
	private String reference;
	private String enquiryDt;
	private AccessPointModel accessPoint = new AccessPointModel();
	private UserModel updatedBy = new UserModel();
	private UserModel createdBy = new UserModel();
	private String remarks;
	private String lastAction;
	private String status;
	private ArrayList<ActionModel> actions = new ArrayList<ActionModel>(0);
	private ArrayList<OfferItemModel> items = new ArrayList<OfferItemModel>(0);
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public CustomerModel getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}
	public long getEnquiryNo() {
		return enquiryNo;
	}
	public void setEnquiryNo(long enquiryNo) {
		this.enquiryNo = enquiryNo;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getEnquiryDt() {
		return enquiryDt;
	}
	public void setEnquiryDt(String enquiryDt) {
		this.enquiryDt = enquiryDt;
	}
	public AccessPointModel getAccessPoint() {
		return accessPoint;
	}
	public void setAccessPoint(AccessPointModel accessPoint) {
		this.accessPoint = accessPoint;
	}
	public UserModel getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(UserModel updatedBy) {
		this.updatedBy = updatedBy;
	}
	public UserModel getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserModel createdBy) {
		this.createdBy = createdBy;
	}
	public String getRemarks() {
		return remarks != null ? remarks : "";
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getLastAction() {
		return lastAction;
	}
	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}
	public ActionModel[] getActions() {
		return (ActionModel[])actions.toArray(new ActionModel[0]);
	}
	public void addAction(ActionModel action) {
		this.actions.add(action);
	}
	public OfferItemModel[] getItem() {
		return (OfferItemModel[])items.toArray(new OfferItemModel[0]);
	}
	public void addItem(ItemModel item, int qty, String offerNo){
		this.items.add(new OfferItemModel(item,qty,offerNo));
	}

}

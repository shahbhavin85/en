package com.en.model;

import com.en.util.ActionType;
import com.en.util.Constant;
import com.en.util.Utils;

public class ActionModel implements Constant {
	
	private int actionNo;
	private String actionDt;
	private UserModel addedBy = new UserModel();
	private String actionType;
	private UserModel salesman = new UserModel();
	private String appDt;
	private String appTm;
	private String inTm;
	private String outTm;
	private String remarks;
	private AccessPointModel prevACPoint = new AccessPointModel();
	private AccessPointModel newACPoint = new AccessPointModel();
	private UserModel owner = new UserModel();
	public AccessPointModel getPrevACPoint() {
		return prevACPoint;
	}
	public void setPrevACPoint(AccessPointModel prevACPoint) {
		this.prevACPoint = prevACPoint;
	}
	public AccessPointModel getNewACPoint() {
		return newACPoint;
	}
	public void setNewACPoint(AccessPointModel newACPoint) {
		this.newACPoint = newACPoint;
	}
	public int getActionNo() {
		return actionNo;
	}
	public void setActionNo(int actionNo) {
		this.actionNo = actionNo;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public UserModel getSalesman() {
		return salesman;
	}
	public void setSalesman(UserModel salesman) {
		this.salesman = salesman;
	}
	public String getAppDt() {
		return appDt;
	}
	public void setAppDt(String appDt) {
		this.appDt = appDt;
	}
	public String getAppTm() {
		return appTm;
	}
	public void setAppTm(String appTm) {
		this.appTm = appTm;
	}
	public String getAppAppTime() {
		return Utils.getAppTimeFormat(this.appTm);
	}
	public String getInTm() {	
		return inTm;
	}
	public String getAppInTime() {
		return Utils.getAppTimeFormat(this.inTm);
	}
	public void setInTm(String inTm) {
		this.inTm = inTm;
	}
	public String getOutTm() {
		return outTm;
	}
	public String getAppOutTime() {
		return Utils.getAppTimeFormat(this.outTm);
	}
	public void setOutTm(String outTm) {
		this.outTm = outTm;
	}
	public String getRemarks() {
		return (remarks == null || remarks.trim().equals("")) ? "--" : remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public UserModel getOwner() {
		return owner;
	}
	public void setOwner(UserModel owner) {
		this.owner = owner;
	}
	public String getActionDt() {
		return actionDt;
	}
	public void setActionDt(String actionDt) {
		this.actionDt = actionDt;
	}
	public UserModel getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(UserModel addedBy) {
		this.addedBy = addedBy;
	}
	
	public String getDesc(){
		StringBuffer desc = new StringBuffer(0);
		if(this.actionType.equals(ActionType.APPOINTMENT)){
			desc.append("Appointment with Customer. <br/> Saleman : "+this.salesman.getUserId()+" <br/> Appointment Dt. : "+Utils.convertToAppDate(this.appDt)+"<br/> Appointment Tm. :"+this.getAppAppTime()+"<br /> Remark :"+this.getRemarks());
		} else if(this.actionType.equals(ActionType.FORWARD)){
			desc.append("Enquiry Fowarded. <br/> Previous Access Point: "+this.prevACPoint.getFullAccess()+" <br/> New Access Point : "+this.newACPoint.getFullAccess()+"<br/> Remark :"+this.getRemarks());
		} else if(this.actionType.equals(ActionType.APPOINTMENT_REPLY)){
			desc.append("Meeting with Customer. <br/> Meeting Dt. : "+Utils.convertToAppDate(this.appDt)+"<br/> In Tm. :"+this.getAppInTime()+"<br/> Out Tm. :"+this.getAppOutTime()+"<br /> Remark :"+this.getRemarks());			
		} else if(this.actionType.equals(ActionType.CLOSED)){
			desc.append("Enquiry Closed. <br/>Remark :"+this.getRemarks());			
		} else if(this.actionType.equals(ActionType.ENQUIRY_ALERT)){
			desc.append("Enquiry Alert. <br/>Alert Date :"+Utils.convertToAppDate(this.getAppDt())+"<br/>Remark :"+this.getRemarks());			
		} else if(this.actionType.equals(ActionType.APPOINTMENT_DELAY)){
			desc.append("Appointment with Customer. <br/> Saleman : "+this.salesman.getUserId()+" <br/> Appointment Dt. : "+Utils.convertToAppDate(this.appDt)+"<br/> Appointment Tm. :"+this.getAppAppTime()+"<br /> Remark :"+this.getRemarks());
		} else if(this.actionType.equals(ActionType.CLOSED_APPROVAL)){
			desc.append("Awaiting approval for closing the Enquiry. <br/>Remark :"+this.getRemarks());			
		} else if(this.actionType.equals(ActionType.APPOINTMENT_CANCEL)){
			desc.append("Appointment Cancelled. <br/>Remark :"+this.getRemarks());			
		} 
		return desc.toString();
	}

}

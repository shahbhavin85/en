package com.en.model;

import com.en.util.Constant;
import com.en.util.Utils;

public class EntryModel implements Constant {

	private int id = 0;
	private int branchId = 0;
	private String entryDate = "";
	private int entryId = 0;
	private boolean isCash = true;
	private double amount = 0;
	private String chqNo = "";
	private String chqDt = "0000-00-00";
	private String bankName = "";
	private int entryType = 0;
	private String remark = "";
	private String crdr = "";
	private CustomerModel customer = new CustomerModel();
	private String custBankName = "";
	private boolean isEditable = true;
	private int billNo = 0;
	private AccessPointModel billBranch = new AccessPointModel();
	private AccessPointModel branch = new AccessPointModel();
	private UserModel staff = new UserModel();
	private double adjAmt = 0;
	private String ledgerDesc = "";
	private double qty = 0;
	private double sentQty = 0;
	private int payMode = 0;
	private String user = "";
	private String userName = "";
	public int getEntryType() {
		return entryType;
	}
	public void setEntryType(int entryType) {
		this.entryType = entryType;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getPayMode() {
		return payMode;
	}
	public void setPayMode(int payMode) {
		this.payMode = payMode;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getSentQty() {
		return sentQty;
	}
	public void setSentQty(double sentQty) {
		this.sentQty = sentQty;
	}
	public String getLedgerDesc() {
		return ledgerDesc;
	}
	public void setLedgerDesc(String ledgerDesc) {
		this.ledgerDesc = ledgerDesc;
	}
	public String getChqDt() {
		return chqDt;
	}
	public void setChqDt(String chqDt) {
		this.chqDt = chqDt;
	}
	public double getAdjAmt() {
		return adjAmt;
	}
	public void setAdjAmt(double adjAmt) {
		this.adjAmt = adjAmt;
	}
	public UserModel getStaff() {
		return staff;
	}
	public void setStaff(UserModel staff) {
		this.staff = staff;
	}
	public AccessPointModel getBillBranch() {
		return billBranch;
	}
	public void setBillBranch(AccessPointModel billBranch) {
		this.billBranch = billBranch;
	}
	public int getBillNo() {
		return billNo;
	}
	public void setBillNo(int billNo) {
		this.billNo = billNo;
	}
	public AccessPointModel getBranch() {
		return branch;
	}
	public void setBranch(AccessPointModel branch) {
		this.branch = branch;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getEntryId() {
		return entryId;
	}
	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
	public boolean isCash() {
		return isCash;
	}
	public void setCash(boolean isCash) {
		this.isCash = isCash;
	}
	public String getChqNo() {
		return chqNo;
	}
	public void setChqNo(String chqNo) {
		this.chqNo = chqNo;
	}
	public CustomerModel getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}
	public String getCustBankName() {
		return custBankName;
	}
	public void setCustBankName(String custBankName) {
		this.custBankName = custBankName;
	}
	public String getDesc(){
		String desc = "";
		if(this.entryType > 50 && this.entryType != 71 && this.entryType != 72 && this.entryType != 73){
			desc = "PAYMODE : "+((this.payMode == 0) ? "CASH" : (this.payMode == 1) ? "CHEQUE" : "NEFT / RTGS")+"<br/>";
		}
		desc = desc + ((!this.bankName.equals("")) ? "HESH Bank : "+this.bankName +"<br/>" : "");
		if(this.entryType == 11 || this.entryType == 81 || this.entryType == 46 || this.entryType == 47 || this.entryType == 48 || this.entryType == 49 || 
				this.entryType == 21 || this.entryType == 22 || this.entryType == 23 || this.entryType == 24 || this.entryType == 1 || this.entryType == 2 || this.entryType == 3 || this.entryType == 4 || this.entryType == 31 || this.entryType == 32 || this.entryType == 41 || this.entryType == 42 || this.entryType == 43 || this.entryType == 44 || this.entryType == 54 || this.entryType == 56 || this.entryType == 55
				|| this.entryType == 71 || this.entryType == 72 || this.entryType == 73 || this.entryType > 50){
			if(!this.customer.getCustomerName().equals(""))
				desc = desc + "Customer : "+this.customer.getCustomerName() + " - " + this.customer.getArea() + " - " + this.customer.getCity() + "<br/>";
			if(this.entryType == 1 || this.entryType == 48 || this.entryType == 43 || this.entryType == 23)
				desc = desc + "Customer Bank / Cheque No - Dt : "+this.custBankName + " / "+this.chqNo +" - "+Utils.convertToAppDateDDMMYY(this.chqDt)+"<br/>";
			if(this.entryType == 72 || this.payMode == 1)
				desc = desc + "Cheque No - Dt : "+this.chqNo +" - "+Utils.convertToAppDateDDMMYY(this.chqDt)+"<br/>";
			if(this.entryType == 73)
				desc = desc + "Transaction No : "+this.chqNo+"<br/>";
			if((this.entryType == 4 || this.entryType == 49 || this.entryType == 44 || this.entryType == 73) && !this.custBankName.equals(""))
				desc = desc + "Customer Bank : "+this.custBankName +"<br/>";
			if(this.entryType == 41 || this.entryType == 42 || this.entryType == 43 || this.entryType == 44)
				desc = desc + "Bill No:" +this.getBillBranch().getBillPrefix()+Utils.padBillNo(this.billNo)+"<br/>";
			if(this.entryType == 46 || this.entryType == 47 || this.entryType == 48 || this.entryType == 49)
				desc = desc + "Labour Bill No:" +this.getBillBranch().getBillPrefix()+"LB"+Utils.padBillNo(this.billNo)+"<br/>";
			if(this.entryType == 71 || this.entryType == 72 || this.entryType == 73)
				desc = desc + "Purchase No:" +this.getBillBranch().getBillPrefix()+"P"+Utils.padBillNo(this.billNo)+"<br/>";
			if((this.entryType == 1 || this.entryType == 2 || this.entryType == 3 || this.entryType == 4) && !this.getBillBranch().getBillPrefix().equals(""))
				desc = desc + "Order No:" +this.getBillBranch().getBillPrefix()+"O"+Utils.padBillNo(this.billNo)+"<br/>";
		}
		if( this.entryType == 5 || this.entryType == 60){
			desc = desc + "Branch : "+((this.getBillBranch().getAccessId() == 2) ? "Accounts" : (this.getBillBranch().getAccessId() == 3) ? "Exhibition" : this.getBillBranch().getAccessName()+" - "+this.getBillBranch().getCity())+"<br/>";
		}
		if( this.entryType == 10 || this.entryType == 58 || this.entryType == 59){
			desc = desc + "Staff : "+this.getStaff().getUserName()+"<br/>";
		}
		if(this.isEditable){
			desc = desc + "<label style='background-color: yellow;'>"+this.remark+"</label>";
		} else {
			desc = desc + this.remark;
		}
		return desc;
	}
	public String getSemiLedgerDesc(){
		String desc = "";
		if(this.entryType > 50 && this.entryType != 71 && this.entryType != 72 && this.entryType != 73){
			desc = "PAYMODE : "+((this.payMode == 0) ? "CASH" : (this.payMode == 1) ? "CHEQUE" : "NEFT / RTGS")+"<br/>";
		}
		desc = desc + ((!this.bankName.equals("")) ? "HESH Bank : "+this.bankName +"<br/>" : "");
		if(this.entryType == 11 || this.entryType == 81 || this.entryType == 46 || this.entryType == 47 || this.entryType == 48 || this.entryType == 49 || 
				this.entryType == 21 || this.entryType == 22 || this.entryType == 23 || this.entryType == 24 || this.entryType == 1 || this.entryType == 2 || this.entryType == 3 || this.entryType == 4 || this.entryType == 31 || this.entryType == 32 || this.entryType == 41 || this.entryType == 42 || this.entryType == 43 || this.entryType == 44 || this.entryType == 54 || this.entryType == 56 || this.entryType == 55
				|| this.entryType == 71 || this.entryType == 72 || this.entryType == 73 || this.entryType > 50){
			if(this.entryType == 1 || this.entryType == 48 || this.entryType == 43 || this.entryType == 23)
				desc = desc + "Customer Bank : "+this.custBankName + " Cheque No : "+this.chqNo +" Dt : "+Utils.convertToAppDateDDMMYY(this.chqDt)+"<br/>";
			if(this.entryType == 72 || this.payMode == 1)
				desc = desc + " Cheque No : "+this.chqNo +" Dt : "+Utils.convertToAppDateDDMMYY(this.chqDt)+"<br/>";
			if(this.entryType == 73)
				desc = desc + "Transaction No : "+this.chqNo+"<br/>";
			if((this.entryType == 4 || this.entryType == 49 || this.entryType == 44 || this.entryType == 73) && !this.custBankName.equals(""))
				desc = desc + "Customer Bank : "+this.custBankName +"<br/>";
			if(this.entryType == 41 || this.entryType == 42 || this.entryType == 43 || this.entryType == 44)
				desc = desc + "Bill No:" +this.getBillBranch().getBillPrefix()+Utils.padBillNo(this.billNo)+"<br/>";
			if(this.entryType == 46 || this.entryType == 47 || this.entryType == 48 || this.entryType == 49)
				desc = desc + "Labour Bill No:" +this.getBillBranch().getBillPrefix()+"LB"+Utils.padBillNo(this.billNo)+"<br/>";
			if(this.entryType == 71 || this.entryType == 72 || this.entryType == 73)
				desc = desc + "Purchase No:" +this.getBillBranch().getBillPrefix()+"P"+Utils.padBillNo(this.billNo)+"<br/>";
			if((this.entryType == 1 || this.entryType == 2 || this.entryType == 3 || this.entryType == 4) && !this.getBillBranch().getBillPrefix().equals(""))
				desc = desc + "Order No:" +this.getBillBranch().getBillPrefix()+"O"+Utils.padBillNo(this.billNo)+"<br/>";
		}
		if( this.entryType == 5 || this.entryType == 60){
			desc = desc + "Branch : "+((this.getBillBranch().getAccessId() == 2) ? "Accounts" : (this.getBillBranch().getAccessId() == 3) ? "Exhibition" : this.getBillBranch().getAccessName()+" - "+this.getBillBranch().getCity())+"<br/>";
		}
		if( this.entryType == 10 || this.entryType == 58 || this.entryType == 59){
			desc = desc + "Staff : "+this.getStaff().getUserName()+"<br/>";
		}
		desc = desc + this.remark;
		desc = desc + "<br/>";
		return desc;
	}
	public String getSemiDesc(){
		String desc = "";
		if(this.entryType > 50 && this.entryType != 71 && this.entryType != 72 && this.entryType != 73){
			desc = "PAYMODE : "+((this.payMode == 0) ? "CASH" : (this.payMode == 1) ? "CHEQUE" : "NEFT / RTGS")+"<br/>";
		}
		desc = desc + ((!this.bankName.equals("")) ? "HESH Bank : "+this.bankName +"<br/>" : "");
		if(this.entryType == 11 || this.entryType == 81 || this.entryType == 46 || this.entryType == 47 || this.entryType == 48 || this.entryType == 49 || 
				this.entryType == 21 || this.entryType == 22 || this.entryType == 23 || this.entryType == 24 || this.entryType == 1 || this.entryType == 2 || this.entryType == 3 || this.entryType == 4 || this.entryType == 31 || this.entryType == 32 || this.entryType == 41 || this.entryType == 42 || this.entryType == 43 || this.entryType == 44 || this.entryType == 54 || this.entryType == 56 || this.entryType == 55
				|| this.entryType == 71 || this.entryType == 72 || this.entryType == 73 || this.entryType > 50){
			if(!this.customer.getCustomerName().equals(""))
				desc = desc + "Customer : "+this.customer.getCustomerName() + " - " + this.customer.getArea() + " - " + this.customer.getCity() + "<br/>";
			if(this.entryType == 1 || this.entryType == 48 || this.entryType == 43 || this.entryType == 23)
				desc = desc + "Customer Bank / Cheque No - Dt : "+this.custBankName + " / "+this.chqNo +" - "+Utils.convertToAppDateDDMMYY(this.chqDt)+"<br/>";
			if(this.entryType == 72 || this.payMode == 1)
				desc = desc + "Cheque No - Dt : "+this.chqNo +" - "+Utils.convertToAppDateDDMMYY(this.chqDt)+"<br/>";
			if(this.entryType == 73)
				desc = desc + "Transaction No : "+this.chqNo+"<br/>";
			if((this.entryType == 4 || this.entryType == 49 || this.entryType == 44 || this.entryType == 73) && !this.custBankName.equals(""))
				desc = desc + "Customer Bank : "+this.custBankName +"<br/>";
			if(this.entryType == 41 || this.entryType == 42 || this.entryType == 43 || this.entryType == 44)
				desc = desc + "Bill No:" +this.getBillBranch().getBillPrefix()+Utils.padBillNo(this.billNo)+"<br/>";
			if(this.entryType == 46 || this.entryType == 47 || this.entryType == 48 || this.entryType == 49)
				desc = desc + "Labour Bill No:" +this.getBillBranch().getBillPrefix()+"LB"+Utils.padBillNo(this.billNo)+"<br/>";
			if(this.entryType == 71 || this.entryType == 72 || this.entryType == 73)
				desc = desc + "Purchase No:" +this.getBillBranch().getBillPrefix()+"P"+Utils.padBillNo(this.billNo)+"<br/>";
			if((this.entryType == 1 || this.entryType == 2 || this.entryType == 3 || this.entryType == 4) && !this.getBillBranch().getBillPrefix().equals(""))
				desc = desc + "Order No:" +this.getBillBranch().getBillPrefix()+"O"+Utils.padBillNo(this.billNo)+"<br/>";
		}
		if( this.entryType == 5 || this.entryType == 60){
			desc = desc + "Branch : "+((this.getBillBranch().getAccessId() == 2) ? "Accounts" : (this.getBillBranch().getAccessId() == 3) ? "Exhibition" : this.getBillBranch().getAccessName()+" - "+this.getBillBranch().getCity())+"<br/>";
		}
		if( this.entryType == 10 || this.entryType == 58 || this.entryType == 59){
			desc = desc + "Staff : "+this.getStaff().getUserName()+"<br/>";
		}
		if(this.isEditable){
			desc = desc + "<label style='background-color: yellow;'>"+this.remark+"</label>";
		} else {
			desc = desc + this.remark;
		}
		return desc;
	}
	public String getCrdr() {
		return crdr;
	}
	public String getDisplayCrdr() {
		return (crdr.equals("C")) ? "Cr." : "Dr.";
	} 
	public void setCrdr(String crdr) {
		this.crdr = crdr;
	}
	public boolean isEditable() {
		return isEditable;
	}
	public void setEditable() {
		this.isEditable = false;
	}
	public String getBillRefNo(){
		String desc = "";
		if(this.entryType == 41 || this.entryType == 42 || this.entryType == 43 || this.entryType == 44)
			desc = this.getBillBranch().getBillPrefix()+Utils.padBillNo(this.billNo);
		if(this.entryType == 71 || this.entryType == 72 || this.entryType == 73)
			desc = this.getBillBranch().getBillPrefix()+"P"+Utils.padBillNo(this.billNo);
		if((this.entryType == 1 || this.entryType == 2 || this.entryType == 3 || this.entryType == 4) && !this.getBillBranch().getBillPrefix().equals(""))
			desc = this.getBillBranch().getBillPrefix()+"O"+Utils.padBillNo(this.billNo);
		return desc;
	}
}

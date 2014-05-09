package com.en.model;


public class CustomerModel extends CustomerGroupModel {

	private int customerId = 0;
	private String contactPerson= "";
	private String customerName = "";
	private String customerType = "";
	private String address = "";
	private String area = "";
	private String city = "";
	private String district = "";
	private String state = "";
	private String pincode = "";
	private String stdcode = "";
	private String phone1 = "";
	private String phone2 = "";
	private String mobile1 = "";
	private String mobile2 = "";
	private String grade = "";
	private String email = "";
	private String email1 = "";
	private String email2 = "";
	private String email3 = "";
	private String email4 = "";
	private String email5 = "";
	private String website = "";
	private String tin = "";
	private String cst = "";
	private String transport = "";
	private String remark = "";
	private int billCount = 0;
	private boolean isSupplier = false;
	private double openBal = 0;
	private String openBalRemark = "";
	private UserModel collectionPerson = new UserModel();
	private long txnCnt = 0;
	private boolean checked = false;
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getOpenBalRemark() {
		return openBalRemark;
	}
	public void setOpenBalRemark(String openBalRemark) {
		this.openBalRemark = openBalRemark;
	}
	public long getTxnCnt() {
		return txnCnt;
	}
	public void setTxnCnt(long txnCnt) {
		this.txnCnt = txnCnt;
	}
	public UserModel getCollectionPerson() {
		return collectionPerson;
	}
	public void setCollectionPerson(UserModel collectionPerson) {
		this.collectionPerson = collectionPerson;
	}
	public int getBillCount() {
		return billCount;
	}
	public void setBillCount(int billCount) {
		this.billCount = billCount;
	}
	public String getEmail1() {
		return email1.toUpperCase();
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public double getOpenBal() {
		return openBal;
	}
	public void setOpenBal(double openBal) {
		this.openBal = openBal;
	}
	public boolean isSupplier() {
		return isSupplier;
	}
	public void setSupplier(boolean isSupplier) {
		this.isSupplier = isSupplier;
	}
	public String getContactPerson() {
		return contactPerson.toUpperCase();
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getCustomerName() {
		return customerName.toUpperCase();
	}
	public String getFullCustomerName() {
		return (customerName+" - "+this.area+" - "+this.city).toUpperCase();
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerType() {
		return customerType.toUpperCase();
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getAddress() {
		return address.toUpperCase();
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getArea() {
		return area.toUpperCase();
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCity() {
		return city.toUpperCase();
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district.toUpperCase();
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state.toUpperCase();
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode.toUpperCase();
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getStdcode() {
		return stdcode.toUpperCase();
	}
	public void setStdcode(String stdcode) {
		this.stdcode = stdcode;
	}
	public String getPhone1() {
		return phone1.toUpperCase();
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2.toUpperCase();
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getMobile1() {
		return mobile1.toUpperCase();
	}
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
	public String getMobile2() {
		return mobile2.toUpperCase();
	}
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade.toUpperCase();
	}
	public String getEmail() {
		return email.toUpperCase();
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getEmail3() {
		return email3;
	}
	public void setEmail3(String email3) {
		this.email3 = email3;
	}
	public String getEmail4() {
		return email4;
	}
	public void setEmail4(String email4) {
		this.email4 = email4;
	}
	public String getEmail5() {
		return email5;
	}
	public void setEmail5(String email5) {
		this.email5 = email5;
	}
	public String getWebsite() {
		return website.toUpperCase();
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getTin() {
		return tin.toUpperCase();
	}
	public void setTin(String tin) {
		this.tin = tin.toUpperCase();
	}
	public String getCst() {
		return cst;
	}
	public void setCst(String cst) {
		this.cst = cst.toUpperCase();
	}
	public String getTransport() {
		return transport.toUpperCase();
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getRemark() {
		return remark.toUpperCase();
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
 	public String getId() {
		return this.state.toUpperCase()+"!"+this.customerType+"<"+this.tin +">"+this.cst+"~"+this.contactPerson+"|"+this.mobile1;
	}
	public String getLabel(){
 		String lbl = "";
 		lbl = "<b>"+customerName+"</b><br/>";
 		lbl = lbl + (address.equals("") ? "" : address +"<br/>");
// 		lbl = lbl + (area.equals("") ? "" : area +"<br/>");
 		lbl = lbl + city+(pincode.equals("") ? "" : " - "+pincode)+"<br/>";
 		lbl = lbl + state;
 		return lbl.toUpperCase();
 	}
	public String getLedgerLabel(){
 		String lbl = "";
 		lbl = "<font style='font-size:13px;'><b>"+customerName+"</b></font><br/>";
 		lbl = lbl + "<font style='font-size:8px;'>" + (address.equals("") ? "" : address +"</font><br/>");
// 		lbl = lbl + (area.equals("") ? "" : area +"<br/>");
 		lbl = lbl + "<font style='font-size:8px;'>" + city+(pincode.equals("") ? "" : " - "+pincode)+"</font><br/>";
 		lbl = lbl + "<font style='font-size:8px;'>" + state+"</font>";
 		lbl = lbl + "<font style='font-size:8px;'> Phone : " + stdcode +" - " + phone1 + " / " + phone2 + " Mobile : "+ mobile1 +" / "+ mobile2 +"</font>";
 		return lbl.toUpperCase();
 	}
}

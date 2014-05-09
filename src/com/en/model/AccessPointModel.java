package com.en.model;

/**
 * @author Bhavin
 *
 */
public class AccessPointModel {

	private int accessId = 0;
	private String accessName = "";
	private String billPrefix = "";
	private String address = "";
	private String city = "";
	private String district = "";
	private String state = "";
	private String pincode = "";
	private String stdcode = "";
	private String phone1 = "";
	private String phone2 = "";
	private String mobile1 = "";
	private String mobile2 = "";
	private String email = "";
	private String vat = "";
	private String cst = "";
	private String withTin = "";
	private String noTin = "";
	private String functional = "Y";
	private double openBal = 0;
	private String bankName1 = "";
	private String bankBranch1 = "";
	private String bankAc1 = "";
	private String bankIfsc1 = "";
	private String bankName2 = "";
	private String bankBranch2 = "";
	private String bankAc2 = "";
	private String bankIfsc2 = "";
	public int getAccessId() {
		return accessId;
	}
	public void setAccessId(int accessId) {
		this.accessId = accessId;
	}
	public String getBankName1() {
		return bankName1;
	}
	public void setBankName1(String bankName1) {
		this.bankName1 = bankName1;
	}
	public String getBankBranch1() {
		return bankBranch1;
	}
	public void setBankBranch1(String bankBranch1) {
		this.bankBranch1 = bankBranch1;
	}
	public String getBankAc1() {
		return bankAc1;
	}
	public void setBankAc1(String bankAc1) {
		this.bankAc1 = bankAc1;
	}
	public String getBankIfsc1() {
		return bankIfsc1;
	}
	public void setBankIfsc1(String bankIfsc1) {
		this.bankIfsc1 = bankIfsc1;
	}
	public String getBankName2() {
		return bankName2;
	}
	public void setBankName2(String bankName2) {
		this.bankName2 = bankName2;
	}
	public String getBankBranch2() {
		return bankBranch2;
	}
	public void setBankBranch2(String bankBranch2) {
		this.bankBranch2 = bankBranch2;
	}
	public String getBankAc2() {
		return bankAc2;
	}
	public void setBankAc2(String bankAc2) {
		this.bankAc2 = bankAc2;
	}
	public String getBankIfsc2() {
		return bankIfsc2;
	}
	public void setBankIfsc2(String bankIfsc2) {
		this.bankIfsc2 = bankIfsc2;
	}
	public double getOpenBal() {
		return openBal;
	}
	public void setOpenBal(double openBal) {
		this.openBal = openBal;
	}
	public String getWithTin() {
		return withTin;
	}
	public void setWithTin(String withTin) {
		this.withTin = withTin;
	}
	public String getNoTin() {
		return noTin;
	}
	public void setNoTin(String noTin) {
		this.noTin = noTin;
	}
	public String getAccessName() {
		return accessName.toUpperCase();
	}
	public String getFullAccess() {
		return (this.accessName).toUpperCase();
	}
	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}
	public String getBillPrefix() {
		return billPrefix.toUpperCase();
	}
	public void setBillPrefix(String billPrefix) {
		this.billPrefix = billPrefix;
	}
	public String getAddress() {
		return address.toUpperCase();
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFunctional() {
		return functional;
	}
	public void setFunctional(String functional) {
		this.functional = functional;
	}
	public String getVat() {
		return vat.toUpperCase();
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getCst() {
		return cst.toUpperCase();
	}
	public void setCst(String cst) {
		this.cst = cst;
	}
	
}

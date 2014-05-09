package com.en.model;

import java.util.ArrayList;

public class UserModel {
	
	private String userId = "";
	private String userName = "";
	private String password = "";

	private String mobile1 = "";
	private String phone1 = "";
	private String email1 = "";
	private String salary = "0";
	
	private String dob = "";	
	private String bloodGroup = "";
	private String phone2 = "";
	private String mobile2 = "";
	private String email2 = "";
	private String qualification = "";
	private String personalIdentityMark = "";
	private String address = "";
	private String presentAddress = "";
	private String fatherName = "";
	private String motherName = "";
	private String SpouseName = "";
	private String child1 = "";
	private String child2 = "";
	private String child3 = "";
	private String child4 = "";
	private String doa = "";
	
	private String pastCompany = "";
	private String period = "";
	private String details = "";
	
	private AccessPointModel branch = new AccessPointModel();
	private String manager = "";
	private int type = 1;

	private String bankName1 = "";
	private String bankBranch1 = "";
	private String bankAc1 = "";
	private String bankIfsc1 = "";
	private String bankName2 = "";
	private String bankBranch2 = "";
	private String bankAc2 = "";
	private String bankIfsc2 = "";
	
	private String stdcode = "";
	private String color = "";
	
	private double expense1 = 0;
	private double expense2 = 0;
	private double expense3 = 0;
	private double expense4 = 0;
	private double expense5 = 0;
	
	private ArrayList<String> accessPoints = new ArrayList<String>(0);
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public double getExpense1() {
		return expense1;
	}
	public void setExpense1(double expense1) {
		this.expense1 = expense1;
	}
	public double getExpense2() {
		return expense2;
	}
	public void setExpense2(double expense2) {
		this.expense2 = expense2;
	}
	public double getExpense3() {
		return expense3;
	}
	public void setExpense3(double expense3) {
		this.expense3 = expense3;
	}
	public double getExpense4() {
		return expense4;
	}
	public void setExpense4(double expense4) {
		this.expense4 = expense4;
	}
	public double getExpense5() {
		return expense5;
	}
	public void setExpense5(double expense5) {
		this.expense5 = expense5;
	}
	public String getMobile1() {
		return mobile1;
	}
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getMobile2() {
		return mobile2;
	}
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getPersonalIdentityMark() {
		return personalIdentityMark;
	}
	public void setPersonalIdentityMark(String personalIdentityMark) {
		this.personalIdentityMark = personalIdentityMark;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPresentAddress() {
		return presentAddress;
	}
	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getSpouseName() {
		return SpouseName;
	}
	public void setSpouseName(String spouseName) {
		SpouseName = spouseName;
	}
	public String getChild1() {
		return child1;
	}
	public void setChild1(String child1) {
		this.child1 = child1;
	}
	public String getChild2() {
		return child2;
	}
	public void setChild2(String child2) {
		this.child2 = child2;
	}
	public String getChild3() {
		return child3;
	}
	public void setChild3(String child3) {
		this.child3 = child3;
	}
	public String getChild4() {
		return child4;
	}
	public void setChild4(String child4) {
		this.child4 = child4;
	}
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getPastCompany() {
		return pastCompany;
	}
	public void setPastCompany(String pastCompany) {
		this.pastCompany = pastCompany;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getStdcode() {
		return stdcode;
	}
	public void setStdcode(String stdcode) {
		this.stdcode = stdcode;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public ArrayList<String> getAccessPoints() {
		return accessPoints;
	}
	public void setAccessPoints(ArrayList<String> accessPoints) {
		this.accessPoints = accessPoints;
	}
	public AccessPointModel getBranch() {
		return branch;
	}
	public void setBranch(AccessPointModel branch) {
		this.branch = branch;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void addAccessPoints(String string) {
		this.accessPoints.add(string);
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
	
}

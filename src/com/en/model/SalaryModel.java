package com.en.model;

import java.util.ArrayList;

import com.en.util.Constant;

public class SalaryModel implements Constant {

	private UserModel user = new UserModel();
	private ArrayList<AttendanceModel> attendance = new ArrayList<AttendanceModel>(0);
	private double advances = 0;
	private double expensesApproved = 0;
	private double target = 0;
	private double sales = 0;
	private double collection = 0;
	private double commission = 0;
	private double presentDays = 0;
	private double holiDays = 0;
	private double leaveDays = 0;
	private double leavePending = 0;
	private double latePenalty = 0;
	private double penalty = 0;
	private double ot = 0;
	private String paidOn = "";
	private int month = 0;
	private int year = 0;
	private double expense1 = 0;
	private double expense2 = 0;
	private double expense3 = 0;
	private double expense4 = 0;
	private double expense5 = 0;
	private double advanceAdjust = 0;
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public ArrayList<AttendanceModel> getAttendance() {
		return attendance;
	}
	public void setAttendance(ArrayList<AttendanceModel> attendance) {
		this.attendance = attendance;
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
	public double getAdvanceAdjust() {
		return advanceAdjust;
	}
	public void setAdvanceAdjust(double advanceAdjust) {
		this.advanceAdjust = advanceAdjust;
	}
	public double getAdvances() {
		return advances;
	}
	public void setAdvances(double advances) {
		this.advances = advances;
	}
	public double getExpensesApproved() {
		return expensesApproved;
	}
	public void setExpensesApproved(double expensesApproved) {
		this.expensesApproved = expensesApproved;
	}
	public double getTarget() {
		return target;
	}
	public void setTarget(double target) {
		this.target = target;
	}
	public double getSales() {
		return sales;
	}
	public void setSales(double sales) {
		this.sales = sales;
	}
	public double getCollection() {
		return collection;
	}
	public void setCollection(double collection) {
		this.collection = collection;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}
	public double getPresentDays() {
		return presentDays;
	}
	public void setPresentDays(double presentDays) {
		this.presentDays = presentDays;
	}
	public double getHoliDays() {
		return holiDays;
	}
	public void setHoliDays(double holiDays) {
		this.holiDays = holiDays;
	}
	public double getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(double leaveDays) {
		this.leaveDays = leaveDays;
	}
	public double getLeavePending() {
		return leavePending;
	}
	public void setLeavePending(double leavePending) {
		this.leavePending = leavePending;
	}
	public double getLatePenalty() {
		return latePenalty;
	}
	public void setLatePenalty(double latePenalty) {
		this.latePenalty = latePenalty;
	}
	public double getPenalty() {
		return penalty;
	}
	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}
	public double getOt() {
		return ot;
	}
	public void setOt(double ot) {
		this.ot = ot;
	}
	public String getPaidOn() {
		return paidOn;
	}
	public void setPaidOn(String paidOn) {
		this.paidOn = paidOn;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
}
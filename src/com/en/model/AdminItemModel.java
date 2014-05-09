package com.en.model;

import com.en.util.Utils;

public class AdminItemModel extends ItemModel{
	private double avgCP = 0;
	private double currCP = 0;
	private double avgSP = 0;
	private double currSP = 0;
	private double tt = 0;
	private double SP1 = 0;
	private double SP2 = 0;
	private double SP3 = 0;
	private double lastCP = 0;
	private String refName = "";
	private double wprice = 0;
	private double chinaRate = 0;
	private double serverCP = 0;
	private boolean isIndian = false;
	private String updateDate = "0000-00-00";
	public double getAvgCP() {
		return avgCP;
	}
	public double getServerCP() {
		return serverCP;
	}
	public void setServerCP(double serverCP) {
		this.serverCP = serverCP;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public boolean isIndian() {
		return isIndian;
	}
	public void setIndian() {
		this.isIndian = true;
	}
	public double getWprice() {
		return wprice;
	}
	public void setWprice(double wprice) {
		this.wprice = wprice;
	}
	public double getChinaRate() {
		return chinaRate;
	}
	public void setChinaRate(double chinaRate) {
		this.chinaRate = chinaRate;
	}
	public void setAvgCP(double avgCP) {
		this.avgCP = avgCP;
	}
	public double getLastCP() {
		return lastCP;
	}
	public void setLastCP(double lastCP) {
		this.lastCP = lastCP;
	}
	public String getRefName() {
		return refName;
	}
	public void setRefName(String refName) {
		this.refName = refName;
	}
	public double getCurrCP() {
		return Utils.get2DecimalDouble(currCP);
	}
	public void setCurrCP(double currCP) {
		this.currCP = currCP;
	}
	public double getAvgSP() {
		return Utils.get2DecimalDouble(avgSP);
	}
	public void setAvgSP(double avgSP) {
		this.avgSP = avgSP;
	}
	public double getCurrSP() {
		return Utils.get2DecimalDouble(currSP);
	}
	public void setCurrSP(double currSP) {
		this.currSP = currSP;
	}
	public double getTT() {
		return Utils.get2DecimalDouble(tt);
	}
	public void setTT(double tt) {
		this.tt = tt;
	}
	public double getSP1() {
		return Utils.get2DecimalDouble(SP1);
	}
	public void setSP1(double sP1) {
		SP1 = sP1;
	}
	public double getSP2() {
		return Utils.get2DecimalDouble(SP2);
	}
	public void setSP2(double sP2) {
		SP2 = sP2;
	}
	public double getSP3() {
		return Utils.get2DecimalDouble(SP3);
	}
	public void setSP3(double sP3) {
		SP3 = sP3;
	}
}

package com.en.model;

import com.en.util.Constant;

public class TaxModel implements Constant {
	
	private AccessPointModel accessPoint = new AccessPointModel();
	private double vat1 = 0;
	private double vat2 = 0;
	private double vat3 = 0;
	private double cst1 = 0;
	private double cst2 = 0;
	private double cst3 = 0;
	private double cst1c = 0;
	private double cst2c = 0;
	private double cst3c = 0;
	private double cess = 0;
	public AccessPointModel getAccessPoint() {
		return accessPoint;
	}
	public void setAccessPoint(AccessPointModel accessPoint) {
		this.accessPoint = accessPoint;
	}
	public double getVat1() {
		return vat1;
	}
	public void setVat1(double vat1) {
		this.vat1 = vat1;
	}
	public double getVat2() {
		return vat2;
	}
	public void setVat2(double vat2) {
		this.vat2 = vat2;
	}
	public double getVat3() {
		return vat3;
	}
	public void setVat3(double vat3) {
		this.vat3 = vat3;
	}
	public double getCst1() {
		return cst1;
	}
	public void setCst1(double cst1) {
		this.cst1 = cst1;
	}
	public double getCst2() {
		return cst2;
	}
	public void setCst2(double cst2) {
		this.cst2 = cst2;
	}
	public double getCst3() {
		return cst3;
	}
	public void setCst3(double cst3) {
		this.cst3 = cst3;
	}
	public double getCst1c() {
		return cst1c;
	}
	public void setCst1c(double cst1c) {
		this.cst1c = cst1c;
	}
	public double getCst2c() {
		return cst2c;
	}
	public void setCst2c(double cst2c) {
		this.cst2c = cst2c;
	}
	public double getCst3c() {
		return cst3c;
	}
	public void setCst3c(double cst3c) {
		this.cst3c = cst3c;
	}
	public double getCess() {
		return cess;
	}
	public void setCess(double cess) {
		this.cess = cess;
	}

}

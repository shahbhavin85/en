package com.en.model;

import com.en.util.Constant;

public class CustomerGroupModel implements Constant {
	
	private int custGroupId = 0;
	private String custGroup = "";
	public int getCustGroupId() {
		return custGroupId;
	}
	public void setCustGroupId(int custGroupId) {
		this.custGroupId = custGroupId;
	}
	public String getCustGroup() {
		return custGroup.toUpperCase();
	}
	public void setCustGroup(String custGroup) {
		this.custGroup = custGroup;
	}
}

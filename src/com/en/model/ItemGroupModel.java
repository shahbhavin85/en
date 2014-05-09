package com.en.model;

import com.en.util.Constant;

public class ItemGroupModel implements Constant {
	
	private int itemGroupId = 0;
	private String itemGroup = "";
	public int getItemGroupId() {
		return itemGroupId;
	}
	public void setItemGroupId(int itemGroupId) {
		this.itemGroupId = itemGroupId;
	}
	public String getItemGroup() {
		return itemGroup.toUpperCase();
	}
	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}
}

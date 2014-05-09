package com.en.model;

public class ItemCategoryModel extends ItemGroupModel {

	private int itemCatId = 0;
	private String itemCategory = "";
	public int getItemCatId() {
		return itemCatId;
	}
	public void setItemCatId(int itemCatId) {
		this.itemCatId = itemCatId;
	}
	public String getItemCategory() {
		return itemCategory.toUpperCase();
	}
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
}

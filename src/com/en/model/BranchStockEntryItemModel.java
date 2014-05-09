package com.en.model;

public class BranchStockEntryItemModel {

	private ItemModel item = new ItemModel();
	private double qty = 0;
	private double stock = 0;
	public ItemModel getItem() {
		return item;
	}
	public void setItem(ItemModel item) {
		this.item = item;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getStock() {
		return stock;
	}
	public void setStock(double stock) {
		this.stock = stock;
	}
}

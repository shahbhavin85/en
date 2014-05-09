package com.en.model;

public class OfferItemModel {
	
	private ItemModel item = new ItemModel();
	private int qty = 0;
	private String offerNo;
	public OfferItemModel(ItemModel item, int qty) {
		super();
		this.item= item;
		this.qty = qty;
		this.offerNo = "";
	}
	public OfferItemModel(ItemModel item, int qty, String offerNo) {
		super();
		this.item= item;
		this.qty = qty;
		this.offerNo = offerNo;
	}
	public ItemModel getItem() {
		return item;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getOfferNo() {
		return offerNo;
	}
	public void setOfferNo(String offerNo) {
		this.offerNo = offerNo;
	}

}

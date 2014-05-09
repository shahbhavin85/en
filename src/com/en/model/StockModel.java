package com.en.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.en.util.Constant;

public class StockModel implements Constant {

	private AccessPointModel branch = new AccessPointModel();
	private String fromDate = "0000-00-00";
	private String toDate = "0000-00-00";
	private ArrayList<StockItemModel> items = new ArrayList<StockItemModel>(0);
	private ItemModel item = new ItemModel();
	private HashMap<Integer, StockItemModel> itemMap = new HashMap<Integer, StockItemModel>(0); 
	public AccessPointModel getBranch() {
		return branch;
	}
	public void setBranch(AccessPointModel branch) {
		this.branch = branch;
	}
	public ItemModel getItem() {
		return item;
	}
	public void setItem(ItemModel item) {
		this.item = item;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public ArrayList<StockItemModel> getItems() {
		return items;
	}
	public void setItems(ArrayList<StockItemModel> items) {
		this.items = items;
	}
	public void createMap(){
		StockItemModel[] arr = (StockItemModel[]) this.items.toArray(new StockItemModel[0]);
		for(int i=0; i<arr.length; i++){
			this.itemMap.put(arr[i].getItemId(), arr[i]);
		}
	}
	public StockItemModel getItemModel(int itemId){
		return (this.itemMap.get(itemId) != null) ? this.itemMap.get(itemId) : new StockItemModel();
	}
}

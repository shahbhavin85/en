package com.en.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.en.model.StockItemModel;

public class StockRegisterDateComparator implements Comparator<StockItemModel> {

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public int compare(StockItemModel model1, StockItemModel model2) {
		try {
			Date model1Dt = formatter.parse(model1.getFromDate());
			Date model2Dt = formatter.parse(model2.getFromDate());
			return model1Dt.compareTo(model2Dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}

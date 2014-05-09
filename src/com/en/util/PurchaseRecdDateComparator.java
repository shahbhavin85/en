package com.en.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.en.model.PurchaseModel;

public class PurchaseRecdDateComparator implements Comparator<PurchaseModel> {

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public int compare(PurchaseModel model1, PurchaseModel model2) {
		try {
			Date model1Dt = formatter.parse(model1.getRecdDt());
			Date model2Dt = formatter.parse(model2.getRecdDt());
			return model1Dt.compareTo(model2Dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}

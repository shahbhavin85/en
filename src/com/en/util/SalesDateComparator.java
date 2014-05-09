package com.en.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.en.model.SalesModel;

public class SalesDateComparator implements Comparator<SalesModel> {

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public int compare(SalesModel model1, SalesModel model2) {
		try {
			Date model1Dt = formatter.parse(model1.getSalesdate());
			Date model2Dt = formatter.parse(model2.getSalesdate());
			return model1Dt.compareTo(model2Dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}

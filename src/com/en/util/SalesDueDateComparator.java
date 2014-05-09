package com.en.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.en.model.SalesModel;

public class SalesDueDateComparator implements Comparator<SalesModel>, Constant {

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public int compare(SalesModel model1, SalesModel model2) {
		try {
			Date model1Dt = formatter.parse(model1.getDueDate());
			Date model2Dt = formatter.parse(model2.getDueDate());
			return model1Dt.compareTo(model2Dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}

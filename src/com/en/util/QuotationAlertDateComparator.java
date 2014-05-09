package com.en.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.en.model.QuotationModel;

public class QuotationAlertDateComparator implements Comparator<QuotationModel>, Constant {

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public int compare(QuotationModel model1, QuotationModel model2) {
		try {
			Date model1Dt = formatter.parse(model1.getValidDate());
			Date model2Dt = formatter.parse(model2.getValidDate());
			return model1Dt.compareTo(model2Dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}

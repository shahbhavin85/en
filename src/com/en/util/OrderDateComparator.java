package com.en.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.en.model.OrderModel;

public class OrderDateComparator implements Comparator<OrderModel> , Constant {

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public int compare(OrderModel model1, OrderModel model2) {
		try {
			Date model1Dt = formatter.parse(model1.getOrderDate());
			Date model2Dt = formatter.parse(model2.getOrderDate());
			return model1Dt.compareTo(model2Dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}

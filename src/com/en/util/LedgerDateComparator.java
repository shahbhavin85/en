package com.en.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.en.model.EntryModel;

public class LedgerDateComparator implements Comparator<EntryModel>, Constant {

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public int compare(EntryModel model1, EntryModel model2) {
		try {
			Date model1Dt = formatter.parse(model1.getEntryDate());
			Date model2Dt = formatter.parse(model2.getEntryDate());
			return model1Dt.compareTo(model2Dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}

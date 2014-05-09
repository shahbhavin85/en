package com.en.model;

import com.en.util.Constant;

public class AppointmentModel extends ActionModel implements Constant{
	
	private CustomerModel customer = new CustomerModel();
	private long enqNo = 0;

	public CustomerModel getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}

	public long getEnqNo() {
		return enqNo;
	}

	public void setEnqNo(long enqNo) {
		this.enqNo = enqNo;
	}
}

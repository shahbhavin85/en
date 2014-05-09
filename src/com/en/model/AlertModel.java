package com.en.model;

public class AlertModel extends AppointmentModel {
	
	private AccessPointModel currentpoint = new AccessPointModel();

	public AccessPointModel getCurrentpoint() {
		return currentpoint;
	}

	public void setCurrentpoint(AccessPointModel currentpoint) {
		this.currentpoint = currentpoint;
	}
	
}

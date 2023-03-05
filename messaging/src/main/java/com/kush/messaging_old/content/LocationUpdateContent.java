package com.kush.messaging_old.content;

public class LocationUpdateContent implements Content {
	
	private static final long serialVersionUID = 1L;
	
	private final LocationUpdate locationUpdate;

	public LocationUpdateContent(LocationUpdate locationUpdate) {
		this.locationUpdate = locationUpdate;
	}
	
	public LocationUpdate getLocationUpdate() {
		return locationUpdate;
	}
}

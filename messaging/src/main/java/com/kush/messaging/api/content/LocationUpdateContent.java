package com.kush.messaging.api.content;

import com.kush.service.annotations.Exportable;

@Exportable
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

package com.kush.messaging.api.content;

import java.time.ZonedDateTime;

import com.kush.lib.location.api.Location;
import com.kush.service.annotations.Exportable;

@Exportable
public class LocationUpdate {
	
	private final Location location;
	private final ZonedDateTime time;

	public LocationUpdate(Location location) {
		this.location = location;
		time = ZonedDateTime.now();
	}
	
	public Location getLocation() {
		return location;
	}
	
	public ZonedDateTime getTime() {
		return time;
	}
}

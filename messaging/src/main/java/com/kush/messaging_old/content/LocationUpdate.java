package com.kush.messaging_old.content;

import java.time.LocalDateTime;

import com.kush.lib.location.api.Location;

public class LocationUpdate {
	
	private final Location location;
	private final LocalDateTime time;

	public LocationUpdate(Location location) {
		this.location = location;
		time = LocalDateTime.now();
	}
	
	public Location getLocation() {
		return location;
	}
	
	public LocalDateTime getTime() {
		return time;
	}
}

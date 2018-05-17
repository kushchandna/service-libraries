package com.kush.lib.location.api;

import java.io.Serializable;

import com.kush.service.annotations.Exportable;

@Exportable
public class Place implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final Location location;
    private final String address;

    public Place(String name, Location location, String address) {
        this.name = name;
        this.location = location;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }
}

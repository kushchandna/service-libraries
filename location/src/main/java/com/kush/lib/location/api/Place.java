package com.kush.lib.location.api;

import java.io.Serializable;

import com.kush.lib.service.server.annotations.Exportable;

@Exportable
public interface Place extends Serializable {

    String getName();

    String getAddress();

    Location getLocation();
}

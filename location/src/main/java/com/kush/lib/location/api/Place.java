package com.kush.lib.location.api;

import com.kush.lib.service.server.annotations.Exportable;

@Exportable
public interface Place {

    String getName();

    String getAddress();

    Location getLocation();
}

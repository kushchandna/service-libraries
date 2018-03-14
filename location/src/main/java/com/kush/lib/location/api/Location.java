package com.kush.lib.location.api;

import com.kush.lib.service.server.annotations.Exportable;

@Exportable
public interface Location {

    double getLatitude();

    double getLongitude();
}

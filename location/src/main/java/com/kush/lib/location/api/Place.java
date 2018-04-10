package com.kush.lib.location.api;

import java.io.Serializable;

public interface Place extends Serializable {

    String getName();

    String getAddress();

    Location getLocation();
}

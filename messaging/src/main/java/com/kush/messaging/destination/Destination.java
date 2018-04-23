package com.kush.messaging.destination;

import com.kush.utils.id.Identifier;

public interface Destination {

    DestinationType getType();

    Identifier getId();

    public static enum DestinationType {
        USER,
        GROUP
    }
}

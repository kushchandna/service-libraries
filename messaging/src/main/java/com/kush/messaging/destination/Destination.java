package com.kush.messaging.destination;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifier;

@Exportable
public interface Destination {

    DestinationType getType();

    Identifier getId();

    @Exportable
    public static enum DestinationType {
        USER,
        GROUP
    }
}

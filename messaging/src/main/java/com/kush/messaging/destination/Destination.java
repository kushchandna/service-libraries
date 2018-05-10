package com.kush.messaging.destination;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.commons.AssociatedClasses;
import com.kush.utils.id.Identifier;

@AssociatedClasses({ UserIdBasedDestination.class, GroupIdBasedDestination.class })
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

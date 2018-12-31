package com.kush.messaging.destination;

import java.io.Serializable;

import com.kush.service.annotations.Exportable;
import com.kush.utils.commons.AssociatedClasses;
import com.kush.utils.id.Identifier;

@AssociatedClasses({ UserIdBasedDestination.class, GroupIdBasedDestination.class })
@Exportable
public interface Destination extends Serializable {

    DestinationType getType();

    Identifier getId();

    @Exportable
    public static enum DestinationType {
        USER,
        GROUP
    }
}

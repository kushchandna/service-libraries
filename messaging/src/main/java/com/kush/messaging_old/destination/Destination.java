package com.kush.messaging_old.destination;

import java.io.Serializable;

import com.kush.commons.id.Identifier;
import com.kush.service.annotations.Exportable;
import com.kush.utils.commons.AssociatedClasses;

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

package com.kush.messaging.destination;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifier;

@Exportable
public class UserIdBasedDestination extends IdBasedDestination {

    public UserIdBasedDestination(Identifier id) {
        super(id);
    }

    @Override
    public DestinationType getType() {
        return DestinationType.USER;
    }
}

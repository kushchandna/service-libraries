package com.kush.messaging.destination;

import com.kush.utils.id.Identifier;

public class UserIdBasedDestination extends IdBasedDestination {

    public UserIdBasedDestination(Identifier id) {
        super(id);
    }

    @Override
    public DestinationType getType() {
        return DestinationType.USER;
    }
}

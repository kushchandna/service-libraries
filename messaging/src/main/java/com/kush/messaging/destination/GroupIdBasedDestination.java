package com.kush.messaging.destination;

import com.kush.utils.id.Identifier;

public class GroupIdBasedDestination extends IdBasedDestination {

    public GroupIdBasedDestination(Identifier id) {
        super(id);
    }

    @Override
    public DestinationType getType() {
        return DestinationType.GROUP;
    }
}

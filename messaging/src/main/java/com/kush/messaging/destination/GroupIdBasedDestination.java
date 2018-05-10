package com.kush.messaging.destination;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifier;

@Exportable
public class GroupIdBasedDestination extends IdBasedDestination {

    public GroupIdBasedDestination(Identifier id) {
        super(id);
    }

    @Override
    public DestinationType getType() {
        return DestinationType.GROUP;
    }
}

package com.kush.messaging.destination;

import com.kush.service.annotations.Exportable;
import com.kush.utils.id.Identifier;

@Exportable
public class GroupIdBasedDestination extends IdBasedDestination {

    private static final long serialVersionUID = 1L;

    public GroupIdBasedDestination(Identifier id) {
        super(id);
    }

    @Override
    public DestinationType getType() {
        return DestinationType.GROUP;
    }
}

package com.kush.messaging_old.destination;

import com.kush.commons.id.Identifier;
import com.kush.service.annotations.Exportable;

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

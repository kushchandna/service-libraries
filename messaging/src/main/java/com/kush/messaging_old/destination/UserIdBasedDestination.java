package com.kush.messaging_old.destination;

import com.kush.commons.id.Identifier;
import com.kush.service.annotations.Exportable;

@Exportable
public class UserIdBasedDestination extends IdBasedDestination {

    private static final long serialVersionUID = 1L;

    public UserIdBasedDestination(Identifier id) {
        super(id);
    }

    @Override
    public DestinationType getType() {
        return DestinationType.USER;
    }
}

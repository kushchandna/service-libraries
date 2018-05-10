package com.kush.messaging.destination;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifier;

@Exportable
public abstract class IdBasedDestination implements Destination {

    private final Identifier id;

    public IdBasedDestination(Identifier id) {
        this.id = id;
    }

    @Override
    public Identifier getId() {
        return id;
    }
}

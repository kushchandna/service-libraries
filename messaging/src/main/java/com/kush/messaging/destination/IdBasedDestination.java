package com.kush.messaging.destination;

import com.kush.commons.id.Identifier;
import com.kush.service.annotations.Exportable;

@Exportable
public abstract class IdBasedDestination implements Destination {

    private static final long serialVersionUID = 1L;

    private final Identifier id;

    public IdBasedDestination(Identifier id) {
        this.id = id;
    }

    @Override
    public Identifier getId() {
        return id;
    }
}

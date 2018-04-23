package com.kush.messaging.destination;

import com.kush.utils.id.Identifier;

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

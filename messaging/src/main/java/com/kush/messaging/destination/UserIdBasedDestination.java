package com.kush.messaging.destination;

import com.kush.utils.id.Identifier;

public class UserIdBasedDestination implements Destination {

    private final Identifier userId;

    public UserIdBasedDestination(Identifier userId) {
        this.userId = userId;
    }

    public Identifier getUserId() {
        return userId;
    }
}

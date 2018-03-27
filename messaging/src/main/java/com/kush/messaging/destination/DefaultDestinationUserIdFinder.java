package com.kush.messaging.destination;

import com.kush.utils.id.Identifier;

public class DefaultDestinationUserIdFinder implements DestinationUserIdFinder {

    @Override
    public Identifier getUserId(Destination destination) {
        if (destination instanceof UserIdBasedDestination) {
            return ((UserIdBasedDestination) destination).getUserId();
        }
        throw new IllegalArgumentException();
    }
}

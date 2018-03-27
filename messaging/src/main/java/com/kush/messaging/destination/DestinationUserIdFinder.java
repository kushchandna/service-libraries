package com.kush.messaging.destination;

import com.kush.utils.id.Identifier;

public interface DestinationUserIdFinder {

    Identifier getUserId(Destination destination);
}

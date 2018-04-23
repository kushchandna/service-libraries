package com.kush.messaging.metadata;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.kush.messaging.destination.Destination;
import com.kush.utils.id.Identifier;

public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier sender;
    private final LocalDateTime sentTime;
    private final Set<Destination> destinations;

    public Metadata(Identifier sender, LocalDateTime sentTime, Set<Destination> destinations) {
        this.sender = sender;
        this.sentTime = sentTime;
        this.destinations = new HashSet<>(destinations);
    }

    public Identifier getSender() {
        return sender;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public Set<Destination> getDestinations() {
        return Collections.unmodifiableSet(destinations);
    }
}

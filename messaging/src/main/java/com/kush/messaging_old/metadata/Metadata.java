package com.kush.messaging_old.metadata;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.kush.commons.id.Identifier;
import com.kush.messaging_old.destination.Destination;
import com.kush.service.annotations.Exportable;

@Exportable
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier sender;
    private final LocalDateTime sentTime;
	private final Set<Destination> destinations;

    public Metadata(Identifier sender, LocalDateTime sentTime, Set<Destination> destinations) {
        this.sender = sender;
        this.sentTime = sentTime;
		this.destinations = destinations;
    }

    public Identifier getSender() {
        return sender;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }
    
    public Set<Destination> getDestinations() {
		return destinations;
	}
}

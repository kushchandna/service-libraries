package com.kush.messaging.api;

import java.io.Serializable;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.messaging.api.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.service.annotations.Exportable;

@Exportable
public class Message implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier messageId;
    private final Destination destination;
    private final Content content;
    private final Metadata metadata;

    public Message(Destination destination, Content content, Metadata metadata) {
        this(Identifier.NULL, destination, content, metadata);
    }

    public Message(Identifier messageId, Message message) {
        this(messageId, message.getDestination(), message.getContent(), message.getMetadata());
    }

    public Message(Identifier messageId, Destination destination, Content content, Metadata metadata) {
        this.messageId = messageId;
		this.destination = destination;
        this.content = content;
        this.metadata = metadata;
    }

    @Override
    public Identifier getId() {
        return messageId;
    }
    
    public Destination getDestination() {
		return destination;
	}

    public Content getContent() {
        return content;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (messageId == null ? 0 : messageId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Message other = (Message) obj;
        if (messageId == null) {
            if (other.messageId != null) {
                return false;
            }
        } else if (!messageId.equals(other.messageId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}

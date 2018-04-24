package com.kush.messaging.message;

import java.io.Serializable;

import com.kush.messaging.content.Content;
import com.kush.messaging.metadata.Metadata;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Message implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier id;
    private final Content content;
    private final Metadata metadata;

    public Message(Content content, Metadata metadata) {
        this(Identifier.NULL, content, metadata);
    }

    public Message(Identifier id, Message message) {
        this(id, message.getContent(), message.getMetadata());
    }

    public Message(Identifier id, Content content, Metadata metadata) {
        this.id = id;
        this.content = content;
        this.metadata = metadata;
    }

    @Override
    public Identifier getId() {
        return id;
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
        result = prime * result + (id == null ? 0 : id.hashCode());
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
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}

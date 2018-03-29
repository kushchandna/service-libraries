package com.kush.messaging.message;

import java.io.Serializable;

import com.kush.messaging.content.Content;
import com.kush.messaging.metadata.Metadata;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Message implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier id;
    private final Identifier receiver;
    private final Content content;
    private final Metadata metadata;

    public Message(Identifier receiver, Content content, Metadata metadata) {
        this(Identifier.NULL, receiver, content, metadata);
    }

    public Message(Identifier id, Message message) {
        this(id, message.getReceiver(), message.getContent(), message.getMetadata());
    }

    public Message(Identifier id, Identifier receiver, Content content, Metadata metadata) {
        this.id = id;
        this.receiver = receiver;
        this.content = content;
        this.metadata = metadata;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public Identifier getReceiver() {
        return receiver;
    }

    public Content getContent() {
        return content;
    }

    public Metadata getMetadata() {
        return metadata;
    }
}

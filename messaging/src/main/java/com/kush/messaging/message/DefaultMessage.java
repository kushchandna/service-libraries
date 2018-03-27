package com.kush.messaging.message;

import com.kush.messaging.content.Content;
import com.kush.messaging.metadata.Metadata;

public class DefaultMessage implements Message {

    private static final long serialVersionUID = 1L;

    private final Content content;
    private final Metadata metadata;

    public DefaultMessage(Content content, Metadata metadata) {
        this.content = content;
        this.metadata = metadata;
    }

    @Override
    public Content getContent() {
        return content;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }
}

package com.kush.messaging.metadata;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.utils.id.Identifier;

public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier sender;
    private final LocalDateTime sentTime;

    public Metadata(Identifier sender, LocalDateTime sentTime) {
        this.sender = sender;
        this.sentTime = sentTime;
    }

    public Identifier getSender() {
        return sender;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }
}

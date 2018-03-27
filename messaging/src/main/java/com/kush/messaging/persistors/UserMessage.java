package com.kush.messaging.persistors;

import com.kush.messaging.message.Message;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class UserMessage implements Identifiable {

    private final Identifier id;
    private final Identifier userId;
    private final Message message;

    public UserMessage(Identifier userId, Message message) {
        this(Identifier.NULL, userId, message);
    }

    public UserMessage(Identifier id, UserMessage userMessage) {
        this(id, userMessage.getUserId(), userMessage.getMessage());
    }

    public UserMessage(Identifier id, Identifier userId, Message message) {
        this.id = id;
        this.userId = userId;
        this.message = message;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public Identifier getUserId() {
        return userId;
    }

    public Message getMessage() {
        return message;
    }
}

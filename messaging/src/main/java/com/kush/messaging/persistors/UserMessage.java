package com.kush.messaging.persistors;

import com.kush.messaging.message.Message;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class UserMessage implements Identifiable {

    private final Identifier id;
    private final Identifier receiverUserId;
    private final Identifier senderUserId;
    private final Message message;

    public UserMessage(Identifier receiverUserId, Identifier senderUserId, Message message) {
        this(Identifier.NULL, receiverUserId, senderUserId, message);
    }

    public UserMessage(Identifier id, UserMessage userMessage) {
        this(id, userMessage.getReceiverUserId(), userMessage.getSenderUserId(), userMessage.getMessage());
    }

    public UserMessage(Identifier id, Identifier receiverUserId, Identifier senderUserId, Message message) {
        this.id = id;
        this.receiverUserId = receiverUserId;
        this.senderUserId = senderUserId;
        this.message = message;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public Identifier getReceiverUserId() {
        return receiverUserId;
    }

    public Identifier getSenderUserId() {
        return senderUserId;
    }

    public Message getMessage() {
        return message;
    }
}

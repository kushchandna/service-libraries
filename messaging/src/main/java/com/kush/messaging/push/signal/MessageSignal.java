package com.kush.messaging.push.signal;

import com.kush.messaging.message.Message;
import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.Signal;

public class MessageSignal extends Signal<MessageSignalHandler> {

    private static final long serialVersionUID = 1L;

    private final Message message;

    public MessageSignal(Identifier receiverId, Message message) {
        super(receiverId);
        this.message = message;
    }

    @Override
    protected void handleSignal(MessageSignalHandler messageHandler) {
        messageHandler.handleMessage(message);
    }
}

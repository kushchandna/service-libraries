package com.kush.messaging.push;

import com.kush.messaging.message.Message;
import com.kush.utils.signaling.Signal;

public class MessageSignal extends Signal<MessageHandler> {

    private final Message message;

    public MessageSignal(Message message) {
        this.message = message;
    }

    @Override
    protected void handleSignal(MessageHandler messageHandler) {
        messageHandler.handleMessage(message);
    }
}

package com.kush.messaging.push.signal;

import com.kush.messaging.message.Message;
import com.kush.utils.signaling.Signal;

public class MessageSignal extends Signal<MessageSignalReceiver> {

    private final Message message;

    public MessageSignal(Message message) {
        this.message = message;
    }

    @Override
    protected void handleSignal(MessageSignalReceiver messageHandler) {
        messageHandler.handleMessage(message);
    }
}

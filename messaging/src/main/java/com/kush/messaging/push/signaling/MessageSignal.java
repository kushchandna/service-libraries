package com.kush.messaging.push.signaling;

import com.kush.messaging.message.Message;
import com.kush.utils.signaling.Signal;

public class MessageSignal extends Signal<MessageSignalReceiver> {

    private final Message message;

    public MessageSignal(Message message) {
        this.message = message;
    }

    @Override
    protected void handleSignal(MessageSignalReceiver receiver) {
        receiver.onMessageReceived(message);
    }
}

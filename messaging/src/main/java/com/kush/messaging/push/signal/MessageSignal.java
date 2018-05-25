package com.kush.messaging.push.signal;

import com.kush.messaging.message.Message;
import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.Signal;

public class MessageSignal extends Signal<MessageSignalReceiver> {

    private final Identifier receiverId;
    private final Message message;

    public MessageSignal(Identifier receiverId, Message message) {
        this.receiverId = receiverId;
        this.message = message;
    }

    @Override
    protected void handleSignal(MessageSignalReceiver messageHandler) {
        messageHandler.handleMessage(message);
    }

    @Override
    protected Object getFilter() {
        return receiverId;
    }
}

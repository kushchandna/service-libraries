package com.kush.messaging.push.signal;

import com.kush.messaging.message.Message;
import com.kush.messaging.push.MessageHandler;
import com.kush.utils.signaling.SignalReceiver;

public class MessageSignalReceiver implements SignalReceiver, MessageHandler {

    private final MessageHandler delegate;

    public MessageSignalReceiver(MessageHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleMessage(Message message) {
        delegate.handleMessage(message);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (delegate == null ? 0 : delegate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MessageSignalReceiver other = (MessageSignalReceiver) obj;
        if (delegate == null) {
            if (other.delegate != null) {
                return false;
            }
        } else if (!delegate.equals(other.delegate)) {
            return false;
        }
        return true;
    }
}

package com.kush.messaging_old.push.signal;

import com.kush.messaging_old.message.Message;
import com.kush.messaging_old.push.MessageHandler;
import com.kush.utils.signaling.SignalHandler;

public class MessageSignalHandler implements SignalHandler, MessageHandler {

    private static final long serialVersionUID = 1L;

    private final MessageHandler delegate;

    public MessageSignalHandler(MessageHandler delegate) {
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
        MessageSignalHandler other = (MessageSignalHandler) obj;
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

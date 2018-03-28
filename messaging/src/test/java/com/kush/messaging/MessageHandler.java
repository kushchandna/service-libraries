package com.kush.messaging;

import com.kush.messaging.message.Message;

public interface MessageHandler {

    void handleNewMessage(Message message);
}

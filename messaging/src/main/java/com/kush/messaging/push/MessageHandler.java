package com.kush.messaging.push;

import com.kush.messaging.message.Message;

public interface MessageHandler {

    void handleMessage(Message message);
}

package com.kush.messaging.push;

import com.kush.messaging.message.Message;
import com.kush.service.annotations.Exportable;

@Exportable
public interface MessageHandler {

    void handleMessage(Message message);
}

package com.kush.messaging.push;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.messaging.message.Message;

@Exportable
public interface MessageHandler {

    void handleMessage(Message message);
}

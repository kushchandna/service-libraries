package com.kush.messaging.push;

import java.io.Serializable;

import com.kush.messaging.message.Message;
import com.kush.service.annotations.Exportable;

@Exportable
public interface MessageHandler extends Serializable {

    void handleMessage(Message message);
}

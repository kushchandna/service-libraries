package com.kush.messaging_old.push;

import java.io.Serializable;

import com.kush.messaging_old.message.Message;
import com.kush.service.annotations.Exportable;

@Exportable
public interface MessageHandler extends Serializable {

    void handleMessage(Message message);
}

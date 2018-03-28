package com.kush.messaging.push;

import com.kush.messaging.message.Message;
import com.kush.utils.signaling.SignalReceiver;

public interface MessageHandler extends SignalReceiver {

    void handleMessage(Message message);
}

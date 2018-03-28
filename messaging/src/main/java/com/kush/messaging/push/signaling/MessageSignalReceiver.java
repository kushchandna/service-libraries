package com.kush.messaging.push.signaling;

import com.kush.messaging.message.Message;
import com.kush.utils.signaling.SignalReceiver;

public interface MessageSignalReceiver extends SignalReceiver {

    void onMessageReceived(Message message);
}

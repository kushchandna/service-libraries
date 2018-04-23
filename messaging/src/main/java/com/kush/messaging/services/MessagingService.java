package com.kush.messaging.services;

import java.util.List;
import java.util.Set;

import com.kush.lib.service.server.BaseService;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.message.Message;
import com.kush.messaging.push.MessageHandler;

public class MessagingService extends BaseService {

    public void sendMessage(Content content, Set<Destination> destinations) {
    }

    public List<Message> getAllReceivedMessages() {
        return null;
    }

    public List<Message> getAllSentMessages() {
        return null;
    }

    public void registerMessageHandler(MessageHandler messageHandler) {
    }

    public void unregisterMessageHandler(MessageHandler messageHandler) {
    }
}

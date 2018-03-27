package com.kush.messaging.services;

import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.messaging.content.Content;
import com.kush.messaging.core.Message;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.persistors.UserMessagePersistor;
import com.kush.utils.id.Identifier;

public class MessagingService extends BaseService {

    public void sendMessage(Content content, Destination destination) {
    }

    public List<Message> getRecentMessages(int count) throws ServiceRequestFailedException {
        Identifier userId = getCurrentUser().getId();
        UserMessagePersistor persistor = getInstance(UserMessagePersistor.class);
        try {
            return persistor.fetchRecentMessages(userId, count);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }
}

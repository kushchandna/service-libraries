package com.kush.messaging.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.messaging.MessageHandler;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.DestinationUserIdFinder;
import com.kush.messaging.message.DefaultMessage;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.MapBasedMetadata;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.metadata.MetadataConstants;
import com.kush.messaging.persistors.UserMessagePersistor;
import com.kush.utils.id.Identifier;

public class MessagingService extends BaseService {

    public void sendMessage(Content content, Destination destination) throws ServiceRequestFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        Metadata metadata = prepareMetadata(currentUserId);
        Message message = new DefaultMessage(content, metadata);
        UserMessagePersistor persistor = getInstance(UserMessagePersistor.class);
        DestinationUserIdFinder userIdFinder = getInstance(DestinationUserIdFinder.class);
        Identifier destinationUserId = userIdFinder.getUserId(destination);
        try {
            persistor.addMessage(destinationUserId, currentUserId, message);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public List<Message> getRecentlyReceivedMessages(int count) throws ServiceRequestFailedException {
        Identifier userId = getCurrentUser().getId();
        UserMessagePersistor persistor = getInstance(UserMessagePersistor.class);
        try {
            return persistor.fetchRecentlyReceivedMessages(userId, count);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public List<Message> getRecentlySentMessages(int count) throws ServiceRequestFailedException {
        Identifier userId = getCurrentUser().getId();
        UserMessagePersistor persistor = getInstance(UserMessagePersistor.class);
        try {
            return persistor.fetchRecentlySentMessages(userId, count);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    private Metadata prepareMetadata(Identifier currentUserId) {
        Map<String, Object> metadataProperties = new HashMap<>();
        metadataProperties.put(MetadataConstants.KEY_SENDER, currentUserId);
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        metadataProperties.put(MetadataConstants.KEY_SENT_TIME, dateTime);
        return new MapBasedMetadata(metadataProperties);
    }

    public void registerMessageHandler(MessageHandler messageHandler) {
    }
}

package com.kush.messaging.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.DestinationUserIdFinder;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.push.MessageSignal;
import com.kush.messaging.push.SignalSpaceProvider;
import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.SignalSpace;

public class MessagingService extends BaseService {

    public Message sendMessage(Content content, Destination destination) throws ServiceRequestFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        Metadata metadata = prepareMetadata(currentUserId);
        Identifier destinationUserId = findDestinationUserId(destination);
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        try {
            Message sentMessage = persistor.addMessage(destinationUserId, content, metadata);
            sendMessageSignal(destinationUserId, sentMessage);
            return sentMessage;
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public List<Message> getRecentlyReceivedMessages(int count) throws ServiceRequestFailedException {
        Identifier userId = getCurrentUser().getId();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        try {
            return persistor.fetchRecentlyReceivedMessages(userId, count);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public List<Message> getRecentlySentMessages(int count) throws ServiceRequestFailedException {
        Identifier userId = getCurrentUser().getId();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        try {
            return persistor.fetchRecentlySentMessages(userId, count);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    public void registerMessageHandler(MessageHandler messageHandler) {
        Identifier currentUserId = getCurrentUser().getId();
        SignalSpace signalSpace = getSignalSpace(currentUserId);
        signalSpace.register(MessageSignal.class, messageHandler);
    }

    public void unregisterMessageHandler(MessageHandler messageHandler) {
        Identifier currentUserId = getCurrentUser().getId();
        SignalSpaceProvider signalSpaceProvider = getInstance(SignalSpaceProvider.class);
        SignalSpace signalSpace = signalSpaceProvider.getSignalSpace(currentUserId);
        signalSpace.unregister(MessageSignal.class, messageHandler);
        if (!signalSpace.hasReceiverForSignal(MessageSignal.class)) {
            signalSpaceProvider.removeSignalSpace(currentUserId);
        }
    }

    private SignalSpace getSignalSpace(Identifier userId) {
        SignalSpaceProvider signalSpaceProvider = getInstance(SignalSpaceProvider.class);
        return signalSpaceProvider.getSignalSpace(userId);
    }

    private Metadata prepareMetadata(Identifier currentUserId) {
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return new Metadata(currentUserId, dateTime);
    }

    private Identifier findDestinationUserId(Destination destination) {
        DestinationUserIdFinder userIdFinder = getInstance(DestinationUserIdFinder.class);
        return userIdFinder.getUserId(destination);
    }

    private void sendMessageSignal(Identifier destinationUserId, Message sentMessage) {
        SignalSpace signalSpace = getSignalSpace(destinationUserId);
        signalSpace.emit(new MessageSignal(sentMessage));
    }
}

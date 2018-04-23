package com.kush.messaging.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.push.MessageSignal;
import com.kush.messaging.push.SignalSpaceProvider;
import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.SignalSpace;

public class MessagingService extends BaseService {

    public void sendMessage(Content content, Set<Destination> destinations) {
        Identifier currentUserId = getCurrentUser().getId();
        Metadata metadata = prepareMetadata(currentUserId, destinations);
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        for (Destination destination : destinations) {
            try {
                Message sentMessage = persistor.addMessage(content, metadata);
                sendMessageSignal(destination, sentMessage);
            } catch (PersistorOperationFailedException | ServiceRequestFailedException e) {
                // TODO notify sender
            }
        }
    }

    public List<Message> getAllMessages() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(persistor.fetchIndividualMessages(currentUserId));
        UserGroupService groupService = getInstance(UserGroupService.class);
        List<Group> groups = groupService.getGroups();
        for (Group group : groups) {
            allMessages.addAll(persistor.fetchMessagesInGroup(group.getId()));
        }
        return allMessages;
    }

    public List<Message> getAllReceivedMessages() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(persistor.fetchIndividualMessages(currentUserId));
        UserGroupService groupService = getInstance(UserGroupService.class);
        List<Group> groups = groupService.getGroups();
        for (Group group : groups) {
            allMessages.addAll(persistor.fetchMessagesInGroup(group.getId()));
        }
        return allMessages;
    }

    public List<Message> getAllSentMessages() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(persistor.fetchIndividualMessages(currentUserId));
        UserGroupService groupService = getInstance(UserGroupService.class);
        List<Group> groups = groupService.getGroups();
        for (Group group : groups) {
            allMessages.addAll(persistor.fetchMessagesInGroup(group.getId()));
        }
        return allMessages;
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

    private Metadata prepareMetadata(Identifier currentUserId, Set<Destination> destinations) {
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return new Metadata(currentUserId, dateTime, destinations);
    }

    private void sendMessageSignal(Destination destination, Message message)
            throws ServiceRequestFailedException, PersistorOperationFailedException {
        switch (destination.getType()) {
        case GROUP:
            UserGroupService groupService = getInstance(UserGroupService.class);
            List<GroupMembership> groupMembers = groupService.getGroupMembers(destination.getId());
            groupMembers.stream().forEach(m -> sendSignal(message, m.getMember()));
        case USER:
            Identifier userId = destination.getId();
            sendSignal(message, userId);
            break;
        default:
            throw new IllegalStateException();
        }
    }

    private void sendSignal(Message message, Identifier userId) {
        SignalSpace signalSpace = getSignalSpace(userId);
        signalSpace.emit(new MessageSignal(message));
    }

    private SignalSpace getSignalSpace(Identifier userId) {
        SignalSpaceProvider signalSpaceProvider = getInstance(SignalSpaceProvider.class);
        return signalSpaceProvider.getSignalSpace(userId);
    }
}

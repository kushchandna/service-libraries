package com.kush.messaging.services;

import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.contacts.services.ContactsService;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.messaging.contacts.MessagingContact;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.push.signal.MessageSignal;
import com.kush.messaging.push.signal.MessageSignalReceiver;
import com.kush.messaging.push.signal.SignalSpaceProvider;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.SignalSpace;

@Service
public class MessagingService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod
    public void sendMessage(Content content, Set<Destination> destinations) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        Metadata metadata = prepareMetadata(currentUserId, destinations);
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        Message sentMessage = persistor.addMessage(content, metadata);
        for (Destination destination : destinations) {
            try {
                sendMessageSignal(destination, sentMessage);
            } catch (ValidationFailedException e) {
                // notify
            }
        }
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Message> getAllMessages() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        Set<Message> allMessages = new LinkedHashSet<>();
        allMessages.addAll(persistor.fetchIndividualMessages(currentUserId));
        UserGroupService groupService = getInstance(UserGroupService.class);
        List<Group> groups = groupService.getGroups();
        for (Group group : groups) {
            allMessages.addAll(persistor.fetchRecentMessagesInGroup(group.getId(), -1));
        }
        return new ArrayList<>(allMessages);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void registerMessageHandler(MessageHandler messageHandler) {
        Identifier currentUserId = getCurrentUser().getId();
        SignalSpace signalSpace = getSignalSpace(currentUserId);
        signalSpace.register(MessageSignal.class, new MessageSignalReceiver(messageHandler));
    }

    @AuthenticationRequired
    @ServiceMethod
    public void unregisterMessageHandler(MessageHandler messageHandler) {
        Identifier currentUserId = getCurrentUser().getId();
        SignalSpaceProvider signalSpaceProvider = getInstance(SignalSpaceProvider.class);
        SignalSpace signalSpace = signalSpaceProvider.getSignalSpace(currentUserId);
        signalSpace.unregister(MessageSignal.class, new MessageSignalReceiver(messageHandler));
        if (!signalSpace.hasReceiverForSignal(MessageSignal.class)) {
            signalSpaceProvider.removeSignalSpace(currentUserId);
        }
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<MessagingContact> getMessagingContacts() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ContactsService contactsService = getInstance(ContactsService.class);
        List<Contact> contacts = contactsService.getContacts();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        List<MessagingContact> messagingContacts = new ArrayList<>(contacts.stream()
            .map(contact -> toMessagingContact(currentUserId, contact, persistor))
            .collect(toList()));
        sort(messagingContacts);
        return messagingContacts;
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Message> getMessagesWithUser(Identifier userId) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        return persistor.fetchRecentMessagesBetweenUsers(currentUserId, userId, -1);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Message> getMessagesInGroup(Identifier groupId) throws PersistorOperationFailedException {
        checkSessionActive();
        MessagePersistor persistor = getInstance(MessagePersistor.class);
        return persistor.fetchRecentMessagesInGroup(groupId, -1);
    }

    private MessagingContact toMessagingContact(Identifier currentUserId, Contact contact, MessagePersistor messagePersistor) {
        Identifiable contactObject = contact.getContactObject();
        try {
            Message latestMessage = getLatestMessage(currentUserId, contactObject, messagePersistor);
            return new MessagingContact(contact, latestMessage);
        } catch (PersistorOperationFailedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Message getLatestMessage(Identifier currentUserId, Identifiable contactObject, MessagePersistor messagePersistor)
            throws PersistorOperationFailedException {
        List<Message> recentMessages;
        Identifier id = contactObject.getId();
        if (contactObject instanceof User) {
            recentMessages = messagePersistor.fetchRecentMessagesBetweenUsers(currentUserId, id, 1);
        } else if (contactObject instanceof Group) {
            recentMessages = messagePersistor.fetchRecentMessagesInGroup(id, 1);
        } else {
            throw new IllegalStateException("Unsupported contact object found");
        }
        if (recentMessages.isEmpty()) {
            return null;
        } else {
            return recentMessages.get(0);
        }
    }

    private Metadata prepareMetadata(Identifier currentUserId, Set<Destination> destinations) {
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return new Metadata(currentUserId, dateTime, destinations);
    }

    private void sendMessageSignal(Destination destination, Message message)
            throws PersistorOperationFailedException, ValidationFailedException {
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

package com.kush.messaging.services;

import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.contacts.services.ContactsService;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.messaging.contacts.MessagingContact;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.persistors.MessagePersister;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.push.signal.MessageSignal;
import com.kush.messaging.push.signal.MessageSignalHandler;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.SignalSpace;

@Service
public class MessagingService extends BaseService {

    private static final int COUNT_LATEST_MESSAGES = 5;

    @AuthenticationRequired
    @ServiceMethod
    public Message sendMessage(Content content, Set<Destination> destinations) throws PersistenceOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        Metadata metadata = prepareMetadata(currentUserId, destinations);
        MessagePersister persistor = getInstance(MessagePersister.class);
        Message sentMessage = persistor.addMessage(content, metadata);
        for (Destination destination : destinations) {
            try {
                sendMessageSignal(destination, sentMessage);
            } catch (ValidationFailedException e) {
                // notify
            }
        }
        return sentMessage;
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Message> getAllMessages() throws PersistenceOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        MessagePersister persistor = getInstance(MessagePersister.class);
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
        SignalSpace signalSpace = getSignalSpace();
        signalSpace.register(MessageSignal.class, new MessageSignalHandler(messageHandler), currentUserId);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void unregisterMessageHandler(MessageHandler messageHandler) {
        Identifier currentUserId = getCurrentUser().getId();
        SignalSpace signalSpace = getSignalSpace();
        signalSpace.unregister(MessageSignal.class, new MessageSignalHandler(messageHandler), currentUserId);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<MessagingContact> getMessagingContacts() throws PersistenceOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ContactsService contactsService = getInstance(ContactsService.class);
        List<Contact> contacts = contactsService.getContacts();
        MessagePersister persistor = getInstance(MessagePersister.class);
        List<MessagingContact> messagingContacts = new ArrayList<>(contacts.stream()
            .map(contact -> toMessagingContact(currentUserId, contact, persistor))
            .collect(toList()));
        sort(messagingContacts);
        return messagingContacts;
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Message> getMessagesWithUser(Identifier userId) throws PersistenceOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        MessagePersister persistor = getInstance(MessagePersister.class);
        return persistor.fetchRecentMessagesBetweenUsers(currentUserId, userId, -1);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Message> getMessagesInGroup(Identifier groupId) throws PersistenceOperationFailedException {
        checkSessionActive();
        MessagePersister persistor = getInstance(MessagePersister.class);
        return persistor.fetchRecentMessagesInGroup(groupId, -1);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(MessagePersister.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
        if (!contextContains(SignalSpace.class)) {
            SignalEmitter signalEmitter = SignalEmitters.newAsyncEmitter();
            SignalSpace signalSpace = new SignalSpace(signalEmitter);
            enrichContext(SignalSpace.class, signalSpace);
        }
    }

    private MessagingContact toMessagingContact(Identifier currentUserId, Contact contact, MessagePersister messagePersistor) {
        Identifiable contactObject = contact.getContactObject();
        try {
            List<Message> recentMessages = getRecentMessages(currentUserId, contactObject, messagePersistor);
            return new MessagingContact(contact, recentMessages);
        } catch (PersistenceOperationFailedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private List<Message> getRecentMessages(Identifier currentUserId, Identifiable contactObject,
            MessagePersister messagePersistor) throws PersistenceOperationFailedException {
        Identifier id = contactObject.getId();
        if (contactObject instanceof User) {
            return messagePersistor.fetchRecentMessagesBetweenUsers(currentUserId, id, COUNT_LATEST_MESSAGES);
        } else if (contactObject instanceof Group) {
            return messagePersistor.fetchRecentMessagesInGroup(id, 1);
        } else {
            throw new IllegalStateException("Unsupported contact object found");
        }
    }

    private Metadata prepareMetadata(Identifier currentUserId, Set<Destination> destinations) {
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return new Metadata(currentUserId, dateTime, destinations);
    }

    private void sendMessageSignal(Destination destination, Message message)
            throws PersistenceOperationFailedException, ValidationFailedException {
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
        SignalSpace signalSpace = getSignalSpace();
        signalSpace.emit(new MessageSignal(userId, message));
    }

    private SignalSpace getSignalSpace() {
        return getInstance(SignalSpace.class);
    }
}

package com.kush.messaging.persistors;

import static com.kush.messaging.destination.Destination.DestinationType.USER;

import java.util.List;
import java.util.function.Predicate;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.Destination.DestinationType;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.ordering.RecentFirst;
import com.kush.utils.id.Identifier;

public class DefaultMessagePersistor extends DelegatingPersistor<Message> implements MessagePersistor {

    public DefaultMessagePersistor(Persistor<Message> delegate) {
        super(delegate);
    }

    @Override
    public Message addMessage(Content content, Metadata metadata) throws PersistorOperationFailedException {
        Message message = new Message(content, metadata);
        return save(message);
    }

    @Override
    public List<Message> fetchIndividualMessages(Identifier userId) throws PersistorOperationFailedException {
        Predicate<Destination> receivedMsgs = d -> USER.equals(d.getType()) && d.getId().equals(userId);
        return filter(m -> m.getMetadata().getDestinations().stream().anyMatch(receivedMsgs)
                || m.getMetadata().getSender().equals(userId));
    }

    @Override
    public List<Message> fetchMessagesInGroup(Identifier groupId) throws PersistorOperationFailedException {
        Predicate<Destination> groupMsgs = d -> DestinationType.GROUP.equals(d.getType()) && d.getId().equals(groupId);
        return filter(m -> m.getMetadata().getDestinations().stream().anyMatch(groupMsgs));
    }

    private List<Message> filter(Predicate<Message> filter) throws PersistorOperationFailedException {
        return fetch(filter, RecentFirst.INSTANCE, -1);
    }
}

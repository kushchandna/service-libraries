package com.kush.messaging_old.persistors;

import static com.kush.messaging_old.destination.Destination.DestinationType.USER;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;
import com.kush.messaging_old.content.Content;
import com.kush.messaging_old.destination.Destination;
import com.kush.messaging_old.destination.Destination.DestinationType;
import com.kush.messaging_old.message.Message;
import com.kush.messaging_old.metadata.Metadata;
import com.kush.messaging_old.ordering.RecentFirst;

public class DefaultMessagePersistor extends DelegatingPersister<Message> implements MessagePersister {

    public DefaultMessagePersistor(Persister<Message> delegate) {
        super(delegate);
    }

    @Override
    public Message addMessage(Content content, Metadata metadata) throws PersistenceOperationFailedException {
        Message message = new Message(content, metadata);
        return save(message);
    }

    @Override
    public List<Message> fetchIndividualMessages(Identifier userId) throws PersistenceOperationFailedException {
        Predicate<Destination> receivedMsgs = d -> USER.equals(d.getType()) && d.getId().equals(userId);
        return filter(m -> m.getMetadata().getDestinations().stream().anyMatch(receivedMsgs)
                || m.getMetadata().getSender().equals(userId), -1);
    }

    @Override
    public List<Message> fetchRecentMessagesInGroup(Identifier groupId, int maximumMessages)
            throws PersistenceOperationFailedException {
        Predicate<Destination> groupMsgs = d -> DestinationType.GROUP.equals(d.getType()) && d.getId().equals(groupId);
        return filter(m -> m.getMetadata().getDestinations().stream().anyMatch(groupMsgs), maximumMessages);
    }

    @Override
    public List<Message> fetchRecentMessagesBetweenUsers(Identifier selfUserId, Identifier otherUserId, int maximumMessages)
            throws PersistenceOperationFailedException {
        Predicate<Message> filterSentMsgs = filterUserIsSender(selfUserId).and(filterUserIsAmongDestinations(otherUserId));
        Predicate<Message> filterReceivedMsgs = filterUserIsSender(otherUserId).and(filterUserIsAmongDestinations(selfUserId));
        return filter(filterSentMsgs.or(filterReceivedMsgs), maximumMessages);
    }

	@Override
	public List<Message> fetchMessages(Identifier forUserId, LocalDateTime newerThan, LocalDateTime notNewerThan) {
		// TODO Auto-generated method stub
		return null;
	}

    private Predicate<Message> filterUserIsSender(Identifier userId) {
        return msg -> msg.getMetadata().getSender().equals(userId);
    }

    private Predicate<Message> filterUserIsAmongDestinations(Identifier selfUserId) {
        return msg -> msg.getMetadata().getDestinations().stream()
            .anyMatch(d -> USER.equals(d.getType()) && d.getId().equals(selfUserId));
    }

    private List<Message> filter(Predicate<Message> filter, int maximumMessages) throws PersistenceOperationFailedException {
        return fetch(filter, RecentFirst.INSTANCE, maximumMessages);
    }
}

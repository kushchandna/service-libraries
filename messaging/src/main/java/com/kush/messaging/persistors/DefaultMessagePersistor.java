package com.kush.messaging.persistors;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.ordering.RecentFirst;
import com.kush.utils.id.Identifier;

public class DefaultMessagePersistor extends DelegatingPersistor<Message> implements MessagePersistor {

    public DefaultMessagePersistor(Persistor<Message> delegate) {
        super(delegate);
    }

    @Override
    public Message addMessage(Identifier destinationUserId, Content content, Metadata metadata)
            throws PersistorOperationFailedException {
        Message message = new Message(destinationUserId, content, metadata);
        return save(message);
    }

    @Override
    public List<Message> fetchRecentlyReceivedMessages(Identifier userId, int count) throws PersistorOperationFailedException {
        Iterator<Message> messages = fetch(m -> m.getReceiver().equals(userId), RecentFirst.INSTANCE, count);
        return Lists.newArrayList(messages);
    }

    @Override
    public List<Message> fetchRecentlySentMessages(Identifier userId, int count) throws PersistorOperationFailedException {
        Iterator<Message> messages = fetch(m -> m.getMetadata().getSender().equals(userId), RecentFirst.INSTANCE, count);
        return Lists.newArrayList(messages);
    }
}

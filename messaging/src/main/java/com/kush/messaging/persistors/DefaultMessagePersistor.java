package com.kush.messaging.persistors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
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

    // TODO add message ordering
    @Override
    public List<Message> fetchRecentlyReceivedMessages(Identifier userId, int count) throws PersistorOperationFailedException {
        List<Message> recentMessages = new ArrayList<>();
        Iterator<Message> allMessage = fetchAll();
        int messagesFound = 0;
        while (allMessage.hasNext()) {
            Message message = allMessage.next();
            if (message.getReceiver().equals(userId)) {
                recentMessages.add(message);
                messagesFound++;
                if (messagesFound == count) {
                    break;
                }
            }
        }
        return recentMessages;
    }

    @Override
    public List<Message> fetchRecentlySentMessages(Identifier userId, int count) throws PersistorOperationFailedException {
        List<Message> recentMessages = new ArrayList<>();
        Iterator<Message> allMessage = fetchAll();
        int messagesFound = 0;
        while (allMessage.hasNext()) {
            Message message = allMessage.next();
            Identifier sender = getSenderUserId(message);
            if (sender.equals(userId)) {
                recentMessages.add(message);
                messagesFound++;
                if (messagesFound == count) {
                    break;
                }
            }
        }
        return recentMessages;
    }

    private Identifier getSenderUserId(Message message) {
        Metadata metadata = message.getMetadata();
        return metadata.getSender();
    }
}

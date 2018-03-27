package com.kush.messaging.persistors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.message.Message;
import com.kush.utils.id.Identifier;

public class DefaultUserMessagePersistor extends DelegatingPersistor<UserMessage> implements UserMessagePersistor {

    public DefaultUserMessagePersistor(Persistor<UserMessage> delegate) {
        super(delegate);
    }

    @Override
    public void addMessage(Identifier receiverUserId, Identifier senderUserId, Message message)
            throws PersistorOperationFailedException {
        UserMessage userMessage = new UserMessage(receiverUserId, senderUserId, message);
        save(userMessage);
    }

    // TODO add message ordering
    @Override
    public List<Message> fetchRecentlyReceivedMessages(Identifier userId, int count) throws PersistorOperationFailedException {
        List<Message> recentMessages = new ArrayList<>();
        Iterator<UserMessage> allUserMessage = fetchAll();
        int messagesFound = 0;
        while (allUserMessage.hasNext()) {
            UserMessage userMessage = allUserMessage.next();
            if (userMessage.getReceiverUserId().equals(userId)) {
                recentMessages.add(userMessage.getMessage());
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
        Iterator<UserMessage> allUserMessage = fetchAll();
        int messagesFound = 0;
        while (allUserMessage.hasNext()) {
            UserMessage userMessage = allUserMessage.next();
            if (userMessage.getSenderUserId().equals(userId)) {
                recentMessages.add(userMessage.getMessage());
                messagesFound++;
                if (messagesFound == count) {
                    break;
                }
            }
        }
        return recentMessages;
    }
}

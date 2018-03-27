package com.kush.messaging.persistors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.core.Message;
import com.kush.utils.id.Identifier;

public class DefaultUserMessagePersistor extends DelegatingPersistor<UserMessage> implements UserMessagePersistor {

    public DefaultUserMessagePersistor(Persistor<UserMessage> delegate) {
        super(delegate);
    }

    // TODO add message ordering
    @Override
    public List<Message> fetchRecentMessages(Identifier userId, int count) throws PersistorOperationFailedException {
        List<Message> recentMessages = new ArrayList<>();
        Iterator<UserMessage> allUserMessage = fetchAll();
        int messagesFound = 0;
        while (allUserMessage.hasNext()) {
            UserMessage userMessage = allUserMessage.next();
            if (userMessage.getUserId().equals(userId)) {
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

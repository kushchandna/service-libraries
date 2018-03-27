package com.kush.messaging.persistors;

import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.message.Message;
import com.kush.utils.id.Identifier;

public interface UserMessagePersistor extends Persistor<UserMessage> {

    void addMessage(Identifier senderUserId, Message message) throws PersistorOperationFailedException;

    List<Message> fetchRecentMessages(Identifier userId, int count) throws PersistorOperationFailedException;
}

package com.kush.messaging.persistors;

import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.message.Message;
import com.kush.utils.id.Identifier;

public interface UserMessagePersistor extends Persistor<UserMessage> {

    void addMessage(Identifier receiverUserId, Identifier senderUserId, Message message) throws PersistorOperationFailedException;

    List<Message> fetchRecentlyReceivedMessages(Identifier userId, int count) throws PersistorOperationFailedException;

    List<Message> fetchRecentlySentMessages(Identifier userId, int count) throws PersistorOperationFailedException;
}

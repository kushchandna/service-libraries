package com.kush.messaging.persistors;

import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.utils.id.Identifier;

public interface MessagePersistor extends Persistor<Message> {

    Message addMessage(Identifier destinationUserId, Content content, Metadata metadata) throws PersistorOperationFailedException;

    List<Message> fetchRecentlyReceivedMessages(Identifier userId, int count) throws PersistorOperationFailedException;

    List<Message> fetchRecentlySentMessages(Identifier userId, int count) throws PersistorOperationFailedException;
}

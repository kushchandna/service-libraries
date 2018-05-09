package com.kush.messaging.persistors;

import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.utils.id.Identifier;

public interface MessagePersistor extends Persistor<Message> {

    Message addMessage(Content content, Metadata metadata) throws PersistorOperationFailedException;

    List<Message> fetchIndividualMessages(Identifier userId) throws PersistorOperationFailedException;

    List<Message> fetchRecentMessagesInGroup(Identifier groupId, int maximumMessages) throws PersistorOperationFailedException;

    List<Message> fetchRecentMessagesBetweenUsers(Identifier selfUserId, Identifier otherUserId, int maximumMessages)
            throws PersistorOperationFailedException;
}

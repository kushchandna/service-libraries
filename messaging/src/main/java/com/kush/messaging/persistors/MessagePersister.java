package com.kush.messaging.persistors;

import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;

public interface MessagePersister extends Persister<Message> {

    Message addMessage(Content content, Metadata metadata) throws PersistenceOperationFailedException;

    List<Message> fetchIndividualMessages(Identifier userId) throws PersistenceOperationFailedException;

    List<Message> fetchRecentMessagesInGroup(Identifier groupId, int maximumMessages) throws PersistenceOperationFailedException;

    List<Message> fetchRecentMessagesBetweenUsers(Identifier selfUserId, Identifier otherUserId, int maximumMessages)
            throws PersistenceOperationFailedException;
}

package com.kush.messaging.persistors;

import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.core.Message;
import com.kush.utils.id.Identifier;

public interface UserMessagePersistor extends Persistor<UserMessage> {

    List<Message> fetchRecentMessages(Identifier userId, int count) throws PersistorOperationFailedException;
}

package com.kush.messaging.persistence;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.Persister;
import com.kush.messaging.api.Message;

public interface MessagePersister extends Persister<Message> {

	Collection<Message> fetchAllGroupMessages(List<Identifier> groupIds, ZonedDateTime newerThan,
			ZonedDateTime notNewerThan);

	Message saveMessage(Message message);
}

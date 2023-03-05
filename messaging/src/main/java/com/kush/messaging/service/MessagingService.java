package com.kush.messaging.service;

import static java.util.stream.Collectors.toList;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.persistors.GroupPersister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;
import com.kush.messaging.api.ClientMetadata;
import com.kush.messaging.api.Message;
import com.kush.messaging.api.Metadata;
import com.kush.messaging.api.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.GroupIdBasedDestination;
import com.kush.messaging.persistence.MessagePersister;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;

@Service
public class MessagingService extends BaseService {

	@AuthenticationRequired
	@ServiceMethod
	public Collection<Message> fetchAllGroupMessages(ZonedDateTime newerThan, ZonedDateTime notNewerThan)
			throws PersistenceOperationFailedException {
		Identifier currentUserId = getCurrentUser().getId();
		return fetchAllGroupMessages(newerThan, notNewerThan, currentUserId);
	}

	@AuthenticationRequired
	@ServiceMethod
	public Message sendMessage(Identifier toGroupId, Content content, ClientMetadata clientMetadata)
			throws PersistenceOperationFailedException, ValidationFailedException {
		Identifier currentUserId = getCurrentUser().getId();
		checkUserCanSendMessage(toGroupId, currentUserId);
		Metadata metadata = prepareMetadata(clientMetadata, currentUserId);
		Destination destination = new GroupIdBasedDestination(toGroupId);
		Message message = new Message(destination, content, metadata);
		MessagePersister messagePersister = getInstance(MessagePersister.class);
		Message sentMessage = messagePersister.saveMessage(message);
		notifyReceivers(sentMessage);
		return sentMessage;
	}
	
	@AuthenticationRequired
	@ServiceMethod
	public Subscription<Message> subscribeNewMessages(ZonedDateTime forNewerThan) throws PersistenceOperationFailedException {
		Identifier currentUserId = getCurrentUser().getId();
		Collection<Message> messages = fetchAllGroupMessages(ZonedDateTime.now(getInstance(Clock.class)), forNewerThan, currentUserId);
		
		return null;
	}

	private Collection<Message> fetchAllGroupMessages(ZonedDateTime newerThan, ZonedDateTime notNewerThan, Identifier currentUserId)
			throws PersistenceOperationFailedException {
		GroupPersister groupPersister = getInstance(GroupPersister.class);
		List<Group> groups = groupPersister.getGroups(currentUserId);
		List<Identifier> groupIds = groups.stream().map(Group::getId).collect(toList());
		MessagePersister messagePersister = getInstance(MessagePersister.class);
		return messagePersister.fetchAllGroupMessages(groupIds, newerThan, notNewerThan);
	}

	private void checkUserCanSendMessage(Identifier toGroupId, Identifier currentUserId) throws ValidationFailedException {
		if (!getInstance(GroupPersister.class).isMember(currentUserId, toGroupId)) {
			throw new ValidationFailedException("User %s is not a member of group %s", currentUserId, toGroupId);
		}
	}

	private Metadata prepareMetadata(ClientMetadata clientMetadata, Identifier currentUserId) {
		Clock clock = getInstance(Clock.class);
		Metadata metadata = new Metadata(currentUserId, ZonedDateTime.now(clock), clientMetadata);
		return metadata;
	}

	private void notifyReceivers(Message sentMessage) {
		// TODO Auto-generated method stub		
	}
	
	private static class MessageSubscription extends BaseSubscription {
		
	}
}

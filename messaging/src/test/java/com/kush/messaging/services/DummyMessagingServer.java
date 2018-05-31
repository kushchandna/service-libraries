package com.kush.messaging.services;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.contacts.entities.Contact;
import com.kush.lib.contacts.persistors.ContactsPersistor;
import com.kush.lib.contacts.persistors.DefaultContactsPersistor;
import com.kush.lib.contacts.services.ContactsService;
import com.kush.lib.group.entities.DefaultGroupPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.messaging.message.Message;
import com.kush.messaging.persistors.DefaultMessagePersistor;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.service.ApplicationServer;
import com.kush.service.Context;
import com.kush.service.ContextBuilder;
import com.kush.service.auth.credentials.DefaultUserCredentialPersistor;
import com.kush.service.auth.credentials.UserCredential;
import com.kush.service.auth.credentials.UserCredentialPersistor;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;

public class DummyMessagingServer {

    private static final int PORT = 8888;

    public static void main(String[] args) throws StartupFailedException {

        Executor executor = Executors.newFixedThreadPool(5);
        ResolutionRequestsReceiver requestReceiver = new SocketBasedResolutionRequestsProcessor(executor, PORT);

        ApplicationServer server = new ApplicationServer(requestReceiver);

        server.registerService(MessagingService.class);
        server.registerService(ContactsService.class);
        server.registerService(UserGroupService.class);

        Persistor<Contact> delegateContactsPersistor = InMemoryPersistor.forType(Contact.class);
        Persistor<UserCredential> delegateCredentialPersistor = InMemoryPersistor.forType(UserCredential.class);
        Persistor<Group> delegateGroupPersistor = InMemoryPersistor.forType(Group.class);
        Persistor<GroupMembership> groupMembershipPersistor = InMemoryPersistor.forType(GroupMembership.class);
        Persistor<Message> delegateMessagingPersistor = InMemoryPersistor.forType(Message.class);
        Context context = ContextBuilder.create()
            .withInstance(UserCredentialPersistor.class, new DefaultUserCredentialPersistor(delegateCredentialPersistor))
            .withInstance(GroupPersistor.class, new DefaultGroupPersistor(delegateGroupPersistor, groupMembershipPersistor))
            .withInstance(ContactsPersistor.class, new DefaultContactsPersistor(delegateContactsPersistor))
            .withInstance(MessagePersistor.class, new DefaultMessagePersistor(delegateMessagingPersistor))
            .build();
        server.start(context);

    }
}

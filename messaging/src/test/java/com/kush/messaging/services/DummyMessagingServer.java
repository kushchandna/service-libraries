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
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;
import com.kush.lib.service.remoting.receiver.socket.ServerSocketServiceRequestReceiver;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.authentication.credential.DefaultUserCredentialPersistor;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.authentication.credential.UserCredentialPersistor;
import com.kush.messaging.message.Message;
import com.kush.messaging.persistors.DefaultMessagePersistor;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.push.signal.SignalSpaceProvider;
import com.kush.utils.signaling.DefaultSignalEmitterFactory;
import com.kush.utils.signaling.SignalEmitterFactory;

public class DummyMessagingServer {

    private static final int PORT = 8888;

    public static void main(String[] args) throws StartupFailedException {

        ApplicationServer server = new ApplicationServer();

        server.registerService(MessagingService.class);
        server.registerService(ContactsService.class);
        server.registerService(UserGroupService.class);

        Executor executor = Executors.newFixedThreadPool(5);
        ServiceRequestReceiver requestReceiver = new ServerSocketServiceRequestReceiver(executor, PORT);
        server.registerServiceRequestReceiver(requestReceiver);

        Persistor<Contact> delegateContactsPersistor = InMemoryPersistor.forType(Contact.class);
        Persistor<UserCredential> delegateCredentialPersistor = InMemoryPersistor.forType(UserCredential.class);
        Persistor<Group> delegateGroupPersistor = InMemoryPersistor.forType(Group.class);
        Persistor<GroupMembership> groupMembershipPersistor = InMemoryPersistor.forType(GroupMembership.class);
        Persistor<Message> delegateMessagingPersistor = InMemoryPersistor.forType(Message.class);
        SignalEmitterFactory emitterFactory = new DefaultSignalEmitterFactory(executor);
        Context context = ContextBuilder.create()
            .withInstance(UserCredentialPersistor.class, new DefaultUserCredentialPersistor(delegateCredentialPersistor))
            .withInstance(GroupPersistor.class, new DefaultGroupPersistor(delegateGroupPersistor, groupMembershipPersistor))
            .withInstance(ContactsPersistor.class, new DefaultContactsPersistor(delegateContactsPersistor))
            .withInstance(MessagePersistor.class, new DefaultMessagePersistor(delegateMessagingPersistor))
            .withInstance(SignalSpaceProvider.class, new SignalSpaceProvider(executor, emitterFactory))
            .build();
        server.start(context);

    }
}

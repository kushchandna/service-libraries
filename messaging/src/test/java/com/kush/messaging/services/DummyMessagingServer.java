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
import com.kush.lib.profile.entities.DefaultProfilePersistor;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.FieldBuilder;
import com.kush.lib.profile.fields.validators.standard.NonEmptyTextValidator;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.services.UserProfileService;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.profile.template.ProfileTemplateBuilder;
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
import com.kush.utils.signaling.RemoteSignalSpace;
import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.client.SignalHandlerRegistrationRequest;

public class DummyMessagingServer {

    private static final int PORT = 8888;

    public static void main(String[] args) throws StartupFailedException {

        Executor executor = Executors.newFixedThreadPool(5);
        ResolutionRequestsReceiver requestReceiver = new SocketBasedResolutionRequestsProcessor(executor, PORT);
        SignalEmitter signalEmitter = SignalEmitters.newAsyncEmitter(executor, executor);
        RemoteSignalSpace signalSpace = new RemoteSignalSpace(executor, signalEmitter);
        requestReceiver.addResolver(SignalHandlerRegistrationRequest.class, signalSpace);

        ApplicationServer server = new ApplicationServer(requestReceiver);

        server.registerService(MessagingService.class);
        server.registerService(ContactsService.class);
        server.registerService(UserGroupService.class);
        server.registerService(UserProfileService.class);

        Field userIdField = new FieldBuilder("userId")
            .withDisplayName("User ID")
            .withNoRepeatitionAllowed()
            .addValidator(new NonEmptyTextValidator())
            .build();
        ProfileTemplate profileTemplate = new ProfileTemplateBuilder()
            .withField(userIdField)
            .build();

        Persistor<Profile> delegateProfilePersistor = InMemoryPersistor.forType(Profile.class);
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
            .withInstance(ProfilePersistor.class, new DefaultProfilePersistor(delegateProfilePersistor))
            .withInstance(SignalSpace.class, signalSpace)
            .withInstance(ProfileTemplate.class, profileTemplate)
            .build();
        server.start(context);

    }
}

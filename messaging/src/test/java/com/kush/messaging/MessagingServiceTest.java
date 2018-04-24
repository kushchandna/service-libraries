package com.kush.messaging;

import static com.google.common.collect.Sets.newHashSet;
import static com.kush.messaging.destination.Destination.DestinationType.GROUP;
import static com.kush.messaging.destination.Destination.DestinationType.USER;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kush.lib.group.entities.DefaultGroupPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseServiceTest;
import com.kush.messaging.content.Content;
import com.kush.messaging.content.TextContent;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.UserIdBasedDestination;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.persistors.DefaultMessagePersistor;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.push.SignalSpaceProvider;
import com.kush.messaging.services.MessagingService;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.DefaultSignalEmitterFactory;
import com.kush.utils.signaling.SignalEmitterFactory;

public class MessagingServiceTest extends BaseServiceTest {

    private MessagingService messagingService;

    private ExecutorService emitterExecutor;

    @Before
    public void beforeEachTest() throws Exception {
        messagingService = new MessagingService();
        registerService(messagingService);

        UserGroupService groupService = new UserGroupService();
        registerService(groupService);

        Persistor<Group> groupPersistor = InMemoryPersistor.forType(Group.class);
        Persistor<GroupMembership> groupMembershipPersistor = InMemoryPersistor.forType(GroupMembership.class);
        addToContext(GroupPersistor.class, new DefaultGroupPersistor(groupPersistor, groupMembershipPersistor));

        Persistor<Message> messagePersistor = InMemoryPersistor.forType(Message.class);
        addToContext(MessagePersistor.class, new DefaultMessagePersistor(messagePersistor));

        emitterExecutor = Executors.newSingleThreadExecutor();
        SignalEmitterFactory emitterFactory = new DefaultSignalEmitterFactory();
        SignalSpaceProvider signalSpaceProvider = new SignalSpaceProvider(emitterExecutor, emitterFactory);
        addToContext(SignalSpaceProvider.class, signalSpaceProvider);
    }

    @After
    public void afterEachTest() throws Exception {
        emitterExecutor.shutdown();
    }

    @Test
    public void sendMessageToSingleUser() throws Exception {
        User[] users = getUsers();
        User user1 = users[0];
        User user2 = users[1];

        String testMessage = "Test Message";

        runAuthenticatedOperation(user1, () -> {
            sendTextMessageToUser(testMessage, user2);
        });


        runAuthenticatedOperation(user2, () -> {
            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(1));
            Message message = allMessages.get(0);
            validateMessageContentAndMetadata(message, user1, testMessage, user2);
        });

        runAuthenticatedOperation(user1, () -> {
            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(1));
            Message message = allMessages.get(0);
            validateMessageContentAndMetadata(message, user1, testMessage, user2);
        });
    }

    @Test
    public void pushNotificationWhenMessagesAreSentIndividually() throws Exception {
        User[] users = getUsers();
        User user1 = users[0];
        User user2 = users[1];
        User user3 = users[2];

        String testMessage1 = "Test Message 1";
        String testMessage2 = "Test Message 2";
        String testMessage3 = "Test Message 3";
        String testMessage4 = "Test Message 4";

        TestMessageHandler messageHandlerUser1 = new TestMessageHandler();
        TestMessageHandler messageHandlerUser2 = new TestMessageHandler();

        runAuthenticatedOperation(user1, () -> {
            messagingService.registerMessageHandler(messageHandlerUser1);
            messageHandlerUser1.setRegistered(true);
        });

        runAuthenticatedOperation(user2, () -> {
            messagingService.registerMessageHandler(messageHandlerUser2);
            messageHandlerUser2.setRegistered(true);
        });

        messageHandlerUser1.reset();
        messageHandlerUser2.reset();
        runAuthenticatedOperation(user3, () -> {
            sendTextMessageToUser(testMessage1, user1);
            messageHandlerUser1.setExpectedMessage(testMessage1, user3, user1);
            sendTextMessageToUser(testMessage2, user2);
            messageHandlerUser2.setExpectedMessage(testMessage2, user3, user2);

            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(2));
            validateMessageContentAndMetadata(allMessages.get(0), user3, testMessage2, user2);
            validateMessageContentAndMetadata(allMessages.get(1), user3, testMessage1, user1);
        });

        runAuthenticatedOperation(user1, () -> {
            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(1));
            validateMessageContentAndMetadata(allMessages.get(0), user3, testMessage1, user1);
        });

        runAuthenticatedOperation(user2, () -> {
            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(1));
            validateMessageContentAndMetadata(allMessages.get(0), user3, testMessage2, user2);
        });

        messageHandlerUser1.waitUntilInvoked("Message handler for user 1 didn't got first call");
        messageHandlerUser2.waitUntilInvoked("Message handler for user 2 didn't got first call");

        runAuthenticatedOperation(user1, () -> {
            messagingService.unregisterMessageHandler(messageHandlerUser1);
            messageHandlerUser1.setRegistered(false);
        });

        messageHandlerUser2.reset();
        runAuthenticatedOperation(user3, () -> {
            sendTextMessageToUser(testMessage3, user1);
            sendTextMessageToUser(testMessage4, user2);
            messageHandlerUser2.setExpectedMessage(testMessage4, user3, user2);

            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(4));
            validateMessageContentAndMetadata(allMessages.get(0), user3, testMessage4, user2);
            validateMessageContentAndMetadata(allMessages.get(1), user3, testMessage3, user1);
            validateMessageContentAndMetadata(allMessages.get(2), user3, testMessage2, user2);
            validateMessageContentAndMetadata(allMessages.get(3), user3, testMessage1, user1);
        });

        runAuthenticatedOperation(user1, () -> {
            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(2));
            validateMessageContentAndMetadata(allMessages.get(0), user3, testMessage3, user1);
            validateMessageContentAndMetadata(allMessages.get(1), user3, testMessage1, user1);
        });

        runAuthenticatedOperation(user2, () -> {
            List<Message> allMessages = messagingService.getAllMessages();
            assertThat(allMessages, hasSize(2));
            validateMessageContentAndMetadata(allMessages.get(0), user3, testMessage4, user2);
            validateMessageContentAndMetadata(allMessages.get(1), user3, testMessage2, user2);
        });

        messageHandlerUser2.waitUntilInvoked("Message handler for user 2 didn't got second call");
    }

    private void validateMessageContentAndMetadata(Message message, User sender, String expectedTextContent,
            Identifiable... receivers) {
        validateMessageContent(message, expectedTextContent);
        validateSenderRelatedInfo(message, sender);
        validateReceiversRelatedInfo(message, receivers);
    }

    private void validateReceiversRelatedInfo(Message message, Identifiable... receivers) {
        Metadata metadata = message.getMetadata();
        Set<Destination> destinations = metadata.getDestinations();
        assertThat(destinations, hasSize(receivers.length));
        Iterator<Destination> destinationIterator = destinations.iterator();
        for (int i = 0; i < destinations.size(); i++) {
            Identifiable receiver = receivers[i];
            Destination destination = destinationIterator.next();
            validateDestination(receiver, destination);
        }
    }

    private void validateMessageContent(Message message, String expectedTextContent) {
        Content sentContent = message.getContent();
        assertThat(sentContent, is(instanceOf(TextContent.class)));
        TextContent sentTextContent = (TextContent) sentContent;
        assertThat(sentTextContent.getText(), is(equalTo(expectedTextContent)));
    }

    private void validateSenderRelatedInfo(Message message, User sender) {
        Metadata metadata2 = message.getMetadata();
        Identifier msgSender = metadata2.getSender();
        assertThat(msgSender, is(equalTo(sender.getId())));
        LocalDateTime msgSentTime = metadata2.getSentTime();
        assertThat(msgSentTime, is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
    }

    private void validateDestination(Identifiable receiver, Destination destination) {
        if (receiver instanceof User) {
            assertThat(destination.getType(), is(equalTo(USER)));
        } else if (receiver instanceof Group) {
            assertThat(destination.getType(), is(equalTo(GROUP)));
        }
        assertThat(destination.getId(), is(equalTo(receiver.getId())));
    }

    private void sendTextMessageToUser(String text, User user) {
        Content content = new TextContent(text);
        Destination destination = new UserIdBasedDestination(user.getId());
        messagingService.sendMessage(content, newHashSet(destination));
    }

    private final class TestMessageHandler implements MessageHandler {

        private CountDownLatch latch;
        private boolean registered = false;
        private String expectedMessage;
        private User expectedMessageSender;
        private Identifiable[] expectedMessageReceivers;

        private Throwable testFailure;

        @Override
        public void handleMessage(Message message) {
            System.out.println("Message received with content - " + message.getContent());
            try {
                if (!registered) {
                    fail("Should not have received message after unregistration");
                }
                validateMessageContentAndMetadata(message, expectedMessageSender, expectedMessage, expectedMessageReceivers);
            } catch (Throwable e) {
                testFailure = e;
            }
            if (latch.getCount() > 0) {
                latch.countDown();
            }
        }

        public void waitUntilInvoked(String errorIfNotInvoked) throws Exception {
            boolean invoked = latch.await(1000, TimeUnit.MILLISECONDS);
            if (!invoked) {
                fail(errorIfNotInvoked);
            }
            if (testFailure != null) {
                throw new AssertionError(testFailure);
            }
        }

        public void reset() {
            latch = new CountDownLatch(1);
            testFailure = null;
        }

        public void setRegistered(boolean registered) {
            this.registered = registered;
        }

        public void setExpectedMessage(String expectedMessage, User expectedMessageSender,
                Identifiable... expectedMessageReceivers) {
            this.expectedMessage = expectedMessage;
            this.expectedMessageSender = expectedMessageSender;
            this.expectedMessageReceivers = expectedMessageReceivers;
        }
    }
}

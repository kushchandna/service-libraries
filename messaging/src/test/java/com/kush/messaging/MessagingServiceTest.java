package com.kush.messaging;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.messaging.content.Content;
import com.kush.messaging.content.TextContent;
import com.kush.messaging.destination.DefaultDestinationUserIdFinder;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.DestinationUserIdFinder;
import com.kush.messaging.destination.UserIdBasedDestination;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.metadata.MetadataConstants;
import com.kush.messaging.persistors.DefaultMessagePersistor;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.push.SignalSpaceProvider;
import com.kush.messaging.services.MessagingService;
import com.kush.service.TestApplicationServer;
import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.DefaultSignalEmitterFactory;
import com.kush.utils.signaling.SignalEmitterFactory;

public class MessagingServiceTest {

    private static final Instant CURRENT_TIME = Instant.now();
    private static final ZoneId CURRENT_ZONE = ZoneId.systemDefault();
    private static final Clock CLOCK = Clock.fixed(CURRENT_TIME, CURRENT_ZONE);

    @Rule
    public TestApplicationServer server = new TestApplicationServer(5) {

        private ExecutorService emitterExecutor;

        @Override
        protected void before() throws Throwable {
            emitterExecutor = Executors.newSingleThreadExecutor();
            super.before();
        };

        @Override
        protected void after() {
            super.after();
            emitterExecutor.shutdown();
        };

        @Override
        protected ContextBuilder createContextBuilder() {
            Persistor<Message> delegate = InMemoryPersistor.forType(Message.class);
            SignalEmitterFactory emitterFactory = new DefaultSignalEmitterFactory();
            SignalSpaceProvider signalSpaceProvider = new SignalSpaceProvider(emitterExecutor, emitterFactory);
            return ContextBuilder.create()
                .withInstance(Clock.class, CLOCK)
                .withInstance(DestinationUserIdFinder.class, new DefaultDestinationUserIdFinder())
                .withInstance(MessagePersistor.class, new DefaultMessagePersistor(delegate))
                .withInstance(SignalSpaceProvider.class, signalSpaceProvider);
        };
    };

    private MessagingService messagingService;

    @Before
    public void beforeEachTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        messagingService = new MessagingService();
        server.registerService(messagingService);
    }

    @Test
    public void sendMessage() throws Exception {
        User[] users = server.getUsers();
        User user1 = users[0];
        User user2 = users[1];

        String testMessage = "Test Message";

        server.runAuthenticatedOperation(user1, () -> {
            sendTestMessage(user2, testMessage);
        });

        server.runAuthenticatedOperation(user2, () -> {
            validateMessageReceived(user1, testMessage);
        });

        server.runAuthenticatedOperation(user1, () -> {
            validateMessageSent(user1, testMessage);
        });
    }

    @Test
    public void pushNotification() throws Exception {
        User[] users = server.getUsers();
        User user1 = users[0];
        User user2 = users[1];
        User user3 = users[2];

        String testMessage1 = "Test Message 1";
        String testMessage2 = "Test Message 2";
        String testMessage3 = "Test Message 3";
        String testMessage4 = "Test Message 4";

        TestMessageHandler messageHandlerUser1 = new TestMessageHandler();
        TestMessageHandler messageHandlerUser2 = new TestMessageHandler();

        server.runAuthenticatedOperation(user1, () -> {
            messagingService.registerMessageHandler(messageHandlerUser1);
            messageHandlerUser1.setRegistered(true);
        });

        server.runAuthenticatedOperation(user2, () -> {
            messagingService.registerMessageHandler(messageHandlerUser2);
            messageHandlerUser2.setRegistered(true);
        });

        CountDownLatch latchUser1 = messageHandlerUser1.resetLatch();
        CountDownLatch latchUser2 = messageHandlerUser2.resetLatch();
        server.runAuthenticatedOperation(user3, () -> {
            sendTestMessage(user1, testMessage1);
            messageHandlerUser1.setExpectedMessage(testMessage1, user3);
            sendTestMessage(user2, testMessage2);
            messageHandlerUser2.setExpectedMessage(testMessage2, user3);
        });

        server.runAuthenticatedOperation(user1, () -> {
            validateMessageReceived(user3, testMessage1);
        });

        server.runAuthenticatedOperation(user2, () -> {
            validateMessageReceived(user3, testMessage2);
        });

        boolean messageHandledUser1 = latchUser1.await(1, TimeUnit.SECONDS);
        if (!messageHandledUser1) {
            fail("Message handler for user 1 didn't got any call");
        }

        boolean messageHandledUser2 = latchUser2.await(1, TimeUnit.SECONDS);
        if (!messageHandledUser2) {
            fail("Message handler for user 2 didn't got any call");
        }

        server.runAuthenticatedOperation(user1, () -> {
            messagingService.unregisterMessageHandler(messageHandlerUser1);
            messageHandlerUser1.setRegistered(false);
        });

        latchUser2 = messageHandlerUser2.resetLatch();
        server.runAuthenticatedOperation(user3, () -> {
            sendTestMessage(user1, testMessage3);
            sendTestMessage(user2, testMessage4);
            messageHandlerUser2.setExpectedMessage(testMessage4, user3);
        });

        server.runAuthenticatedOperation(user1, () -> {
            validateMessageReceived(user3, testMessage1, testMessage3);
        });

        server.runAuthenticatedOperation(user2, () -> {
            validateMessageReceived(user3, testMessage2, testMessage4);
        });

        messageHandledUser2 = latchUser2.await(1, TimeUnit.SECONDS);
        if (!messageHandledUser2) {
            fail("Message handler for user 2 didn't got second call");
        }
    }

    private void validateMessageSent(User sender, String... expectedContentTexts) throws ServiceRequestFailedException {
        List<Message> recentlySentMessages = messagingService.getRecentlySentMessages(5);
        assertThat(recentlySentMessages, hasSize(expectedContentTexts.length));
        for (int i = 0; i < expectedContentTexts.length; i++) {
            Message sentMessage = recentlySentMessages.get(i);
            validateMessageContentAndMetadata(sentMessage, sender, expectedContentTexts[i]);
        }
    }

    private void validateMessageReceived(User sender, String... expectedContentTexts) throws ServiceRequestFailedException {
        List<Message> recentlyReceivedMessages = messagingService.getRecentlyReceivedMessages(5);
        assertThat(recentlyReceivedMessages, hasSize(expectedContentTexts.length));
        for (int i = 0; i < expectedContentTexts.length; i++) {
            Message receivedMessage = recentlyReceivedMessages.get(i);
            validateMessageContentAndMetadata(receivedMessage, sender, expectedContentTexts[i]);
        }
    }

    private void validateMessageContentAndMetadata(Message message, User sender, String expectedContentText) {
        Content sentContent = message.getContent();
        assertThat(sentContent, is(instanceOf(TextContent.class)));
        TextContent sentTextContent = (TextContent) sentContent;
        assertThat(sentTextContent.getText(), is(equalTo(expectedContentText)));

        Metadata sentMetadata = message.getMetadata();
        Identifier sentMsgSender = sentMetadata.getValue(MetadataConstants.KEY_SENDER, Identifier.class);
        assertThat(sentMsgSender, is(equalTo(sender.getId())));
        LocalDateTime sentMsgSentTime = sentMetadata.getValue(MetadataConstants.KEY_SENT_TIME, LocalDateTime.class);
        assertThat(sentMsgSentTime, is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
    }

    private void sendTestMessage(User user, String text) throws ServiceRequestFailedException {
        Content content = new TextContent(text);
        Destination destination = new UserIdBasedDestination(user.getId());
        messagingService.sendMessage(content, destination);
    }

    private final class TestMessageHandler implements MessageHandler {


        private CountDownLatch latch;
        private boolean registered = false;
        private String expectedMessage;
        private User expectedMessageSender;

        @Override
        public void handleMessage(Message message) {
            System.out.println("Message received with content - " + message.getContent());
            validateMessageContentAndMetadata(message, expectedMessageSender, expectedMessage);
            if (latch.getCount() > 0) {
                latch.countDown();
            }
            if (!registered) {
                fail("Should not have received message after unregistration");
            }
        }

        public CountDownLatch resetLatch() {
            latch = new CountDownLatch(1);
            return latch;
        }

        public void setRegistered(boolean registered) {
            this.registered = registered;
        }

        public void setExpectedMessage(String expectedMessage, User expectedMessageSender) {
            this.expectedMessage = expectedMessage;
            this.expectedMessageSender = expectedMessageSender;
        }
    }
}

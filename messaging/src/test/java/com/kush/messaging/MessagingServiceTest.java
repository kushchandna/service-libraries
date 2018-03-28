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
import com.kush.messaging.persistors.DefaultUserMessagePersistor;
import com.kush.messaging.persistors.UserMessage;
import com.kush.messaging.persistors.UserMessagePersistor;
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
    private final Clock clock = Clock.fixed(CURRENT_TIME, CURRENT_ZONE);

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
            Persistor<UserMessage> delegate = InMemoryPersistor.forType(UserMessage.class);
            SignalEmitterFactory emitterFactory = new DefaultSignalEmitterFactory();
            SignalSpaceProvider signalSpaceProvider = new SignalSpaceProvider(emitterExecutor, emitterFactory);
            return ContextBuilder.create()
                .withInstance(Clock.class, clock)
                .withInstance(DestinationUserIdFinder.class, new DefaultDestinationUserIdFinder())
                .withInstance(UserMessagePersistor.class, new DefaultUserMessagePersistor(delegate))
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

        String testMessage1 = "Test Message 1";
        String testMessage2 = "Test Message 2";

        CountDownLatch latch = new CountDownLatch(1);
        TestMessageHandler messageHandler = new TestMessageHandler(latch, testMessage1, user2);

        server.runAuthenticatedOperation(user1, () -> {
            messagingService.registerMessageHandler(messageHandler);
            messageHandler.setRegistered(true);
        });


        server.runAuthenticatedOperation(user2, () -> {
            sendTestMessage(user1, testMessage1);
        });

        server.runAuthenticatedOperation(user1, () -> {
            validateMessageReceived(user2, testMessage1);
        });

        boolean messageHandled = latch.await(100, TimeUnit.MILLISECONDS);
        if (!messageHandled) {
            fail("Message handler didn't got any call");
        }

        server.runAuthenticatedOperation(user1, () -> {
            messagingService.unregisterMessageHandler(messageHandler);
            messageHandler.setRegistered(false);
        });

        server.runAuthenticatedOperation(user2, () -> {
            sendTestMessage(user1, testMessage2);
        });

        server.runAuthenticatedOperation(user1, () -> {
            validateMessageReceived(user2, testMessage1, testMessage2);
        });
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

    private void sendTestMessage(User user2, String text) throws ServiceRequestFailedException {
        Content content1 = new TextContent(text);
        Destination destination1 = new UserIdBasedDestination(user2.getId());
        messagingService.sendMessage(content1, destination1);
    }

    private final class TestMessageHandler implements MessageHandler {

        private final CountDownLatch latch;
        private final String testMessage;
        private final User user;

        private boolean registered = false;

        private TestMessageHandler(CountDownLatch latch, String testMessage, User user) {
            this.latch = latch;
            this.testMessage = testMessage;
            this.user = user;
        }

        @Override
        public void handleMessage(Message message) {
            System.out.println("Message received with content - " + message.getContent());
            validateMessageContentAndMetadata(message, user, testMessage);
            if (latch.getCount() == 1) {
                latch.countDown();
            }
            if (!registered) {
                fail("Should not have received message after unregistration");
            }
        }

        public void setRegistered(boolean registered) {
            this.registered = registered;
        }
    }
}

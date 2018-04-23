package com.kush.messaging;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseServiceTest;
import com.kush.messaging.content.Content;
import com.kush.messaging.content.TextContent;
import com.kush.messaging.destination.DefaultDestinationUserIdFinder;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.DestinationUserIdFinder;
import com.kush.messaging.destination.UserIdBasedDestination;
import com.kush.messaging.message.Message;
import com.kush.messaging.metadata.Metadata;
import com.kush.messaging.persistors.DefaultMessagePersistor;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.push.SignalSpaceProvider;
import com.kush.messaging.services.MessagingService;
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

        Persistor<Message> delegate = InMemoryPersistor.forType(Message.class);
        addToContext(MessagePersistor.class, new DefaultMessagePersistor(delegate));

        emitterExecutor = Executors.newSingleThreadExecutor();
        SignalEmitterFactory emitterFactory = new DefaultSignalEmitterFactory();
        SignalSpaceProvider signalSpaceProvider = new SignalSpaceProvider(emitterExecutor, emitterFactory);
        addToContext(SignalSpaceProvider.class, signalSpaceProvider);

        addToContext(DestinationUserIdFinder.class, new DefaultDestinationUserIdFinder());
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
            sendTextMessageToUser(testMessage, user1);
        });

        runAuthenticatedOperation(user2, () -> {
            validateMessagesReceived(user1, testMessage);
        });

        runAuthenticatedOperation(user1, () -> {
            validateMessagesSent(user1, testMessage);
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

        CountDownLatch latchUser1 = messageHandlerUser1.resetLatch();
        CountDownLatch latchUser2 = messageHandlerUser2.resetLatch();
        runAuthenticatedOperation(user3, () -> {
            sendTextMessageToUser(testMessage1, user1);
            messageHandlerUser1.setExpectedMessage(testMessage1, user3);
            sendTextMessageToUser(testMessage2, user2);
            messageHandlerUser2.setExpectedMessage(testMessage2, user3);
        });

        runAuthenticatedOperation(user1, () -> {
            validateMessagesReceived(user3, testMessage1);
        });

        runAuthenticatedOperation(user2, () -> {
            validateMessagesReceived(user3, testMessage2);
        });

        boolean messageHandledUser1 = latchUser1.await(100, TimeUnit.MILLISECONDS);
        if (!messageHandledUser1) {
            fail("Message handler for user 1 didn't get any call");
        }

        boolean messageHandledUser2 = latchUser1.await(100, TimeUnit.MILLISECONDS);
        if (!messageHandledUser2) {
            fail("Message handler for user 2 didn't get any call");
        }

        runAuthenticatedOperation(user1, () -> {
            messagingService.unregisterMessageHandler(messageHandlerUser1);
            messageHandlerUser1.setRegistered(false);
        });

        latchUser2 = messageHandlerUser2.resetLatch();
        runAuthenticatedOperation(user3, () -> {
            sendTextMessageToUser(testMessage3, user1);
            sendTextMessageToUser(testMessage4, user2);
            messageHandlerUser2.setExpectedMessage(testMessage4, user3);
        });

        runAuthenticatedOperation(user1, () -> {
            validateMessagesReceived(user3, testMessage1, testMessage3);
        });

        runAuthenticatedOperation(user2, () -> {
            validateMessagesReceived(user3, testMessage2, testMessage4);
        });

        messageHandledUser2 = latchUser2.await(100, TimeUnit.MILLISECONDS);
        if (!messageHandledUser2) {
            fail("Message handler for user 2 didn't got second call");
        }
    }

    private void sendTextMessageToUser(String text, User user) {
        Content content = new TextContent(text);
        Destination destination = new UserIdBasedDestination(user.getId());
        messagingService.sendMessage(content, newHashSet(destination));
    }

    private void validateMessagesReceived(User sender, String... expectedContentTexts) throws Exception {
        List<Message> allReceivedMessages = messagingService.getAllReceivedMessages();
        assertThat(allReceivedMessages, hasSize(expectedContentTexts.length));
        for (int i = 0; i < expectedContentTexts.length; i++) {
            Message sentMessage = allReceivedMessages.get(i);
            validateMessageContentAndMetadata(sentMessage, sender, expectedContentTexts[i]);
        }
    }

    private void validateMessagesSent(User sender, String... expectedContentTexts) throws Exception {
        List<Message> allSentMessages = messagingService.getAllSentMessages();
        assertThat(allSentMessages, hasSize(expectedContentTexts.length));
        for (int i = 0; i < expectedContentTexts.length; i++) {
            Message sentMessage = allSentMessages.get(i);
            validateMessageContentAndMetadata(sentMessage, sender, expectedContentTexts[i]);
        }
    }

    private void validateMessageContentAndMetadata(Message message, User sender, String expectedContentText) {
        Content sentContent = message.getContent();
        assertThat(sentContent, is(instanceOf(TextContent.class)));
        TextContent sentTextContent = (TextContent) sentContent;
        assertThat(sentTextContent.getText(), is(equalTo(expectedContentText)));

        Metadata sentMetadata = message.getMetadata();
        Identifier sentMsgSender = sentMetadata.getSender();
        assertThat(sentMsgSender, is(equalTo(sender.getId())));
        LocalDateTime sentMsgSentTime = sentMetadata.getSentTime();
        assertThat(sentMsgSentTime, is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
    }

    private final class TestMessageHandler implements MessageHandler {

        private CountDownLatch latch;
        private boolean registered = false;
        private String expectedMessage;
        private User expectedMessageSender;

        @Override
        public void handleMessage(Message message) {
            System.out.println("Message received with content - " + message.getContent());
            if (!registered) {
                fail("Should not have received message after unregistration");
            }
            validateMessageContentAndMetadata(message, expectedMessageSender, expectedMessage);
            if (latch.getCount() > 0) {
                latch.countDown();
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

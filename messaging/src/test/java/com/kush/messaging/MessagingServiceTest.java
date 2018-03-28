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
import com.kush.messaging.services.MessagingService;
import com.kush.service.TestApplicationServer;
import com.kush.utils.id.Identifier;

public class MessagingServiceTest {

    private static final Instant CURRENT_TIME = Instant.now();
    private static final ZoneId CURRENT_ZONE = ZoneId.systemDefault();
    private final Clock clock = Clock.fixed(CURRENT_TIME, CURRENT_ZONE);

    @Rule
    public TestApplicationServer server = new TestApplicationServer(5) {

        @Override
        protected ContextBuilder createContextBuilder() {
            Persistor<UserMessage> delegate = InMemoryPersistor.forType(UserMessage.class);
            return ContextBuilder.create()
                .withInstance(Clock.class, clock)
                .withInstance(DestinationUserIdFinder.class, new DefaultDestinationUserIdFinder())
                .withInstance(UserMessagePersistor.class, new DefaultUserMessagePersistor(delegate));
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

        String testMessage = "Test Message";

        CountDownLatch latch = new CountDownLatch(1);
        MessageHandler messageHandler = new MessageHandler() {

            @Override
            public void handleNewMessage(Message message) {
                System.out.println("Message received with content - " + message.getContent());
                validateMessageContentAndMetadata(message, user2, testMessage);
                latch.countDown();
            }
        };

        server.runAuthenticatedOperation(user1, () -> {
            messagingService.registerMessageHandler(messageHandler);
        });


        server.runAuthenticatedOperation(user2, () -> {
            sendTestMessage(user1, testMessage);
        });

        boolean messageHandled = latch.await(100, TimeUnit.MILLISECONDS);
        if (!messageHandled) {
            fail("Message handler didn't got any call");
        }

        server.runAuthenticatedOperation(user1, () -> {
            messagingService.registerMessageHandler(messageHandler);
        });
    }

    private void validateMessageSent(User user, String expectedContentText) throws ServiceRequestFailedException {
        List<Message> recentlySentMessages = messagingService.getRecentlySentMessages(5);
        assertThat(recentlySentMessages, hasSize(1));
        Message sentMessage = recentlySentMessages.get(0);
        validateMessageContentAndMetadata(sentMessage, user, expectedContentText);
    }

    private void validateMessageReceived(User user, String expectedContentText) throws ServiceRequestFailedException {
        List<Message> recentlyReceivedMessages = messagingService.getRecentlyReceivedMessages(5);
        assertThat(recentlyReceivedMessages, hasSize(1));
        Message receivedMessage = recentlyReceivedMessages.get(0);
        validateMessageContentAndMetadata(receivedMessage, user, expectedContentText);
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
}

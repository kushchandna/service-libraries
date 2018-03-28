package com.kush.messaging;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
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

        server.runAuthenticatedOperation(user1, () -> {
            Content content1 = new TextContent("Test Message");
            Destination destination1 = new UserIdBasedDestination(user2.getId());
            messagingService.sendMessage(content1, destination1);
        });

        server.runAuthenticatedOperation(user2, () -> {
            List<Message> recentlyReceivedMessages = messagingService.getRecentlyReceivedMessages(5);
            assertThat(recentlyReceivedMessages, hasSize(1));
            Message receivedMessage = recentlyReceivedMessages.get(0);

            Content receivedContent = receivedMessage.getContent();
            assertThat(receivedContent, is(instanceOf(TextContent.class)));
            TextContent receivedTextContent = (TextContent) receivedContent;
            assertThat(receivedTextContent.getText(), is(equalTo("Test Message")));

            Metadata receivedMetadata = receivedMessage.getMetadata();
            Identifier rcvdMsgSender = receivedMetadata.getValue(MetadataConstants.KEY_SENDER, Identifier.class);
            assertThat(rcvdMsgSender, is(equalTo(user1.getId())));
            LocalDateTime rcvdMsgSentTime = receivedMetadata.getValue(MetadataConstants.KEY_SENT_TIME, LocalDateTime.class);
            assertThat(rcvdMsgSentTime, is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
        });

        server.runAuthenticatedOperation(user1, () -> {
            List<Message> recentlySentMessages = messagingService.getRecentlySentMessages(5);
            assertThat(recentlySentMessages, hasSize(1));
            Message sentMessage = recentlySentMessages.get(0);

            Content sentContent = sentMessage.getContent();
            assertThat(sentContent, is(instanceOf(TextContent.class)));
            TextContent sentTextContent = (TextContent) sentContent;
            assertThat(sentTextContent.getText(), is(equalTo("Test Message")));

            Metadata sentMetadata = sentMessage.getMetadata();
            Identifier sentMsgSender = sentMetadata.getValue(MetadataConstants.KEY_SENDER, Identifier.class);
            assertThat(sentMsgSender, is(equalTo(user1.getId())));
            LocalDateTime sentMsgSentTime = sentMetadata.getValue(MetadataConstants.KEY_SENT_TIME, LocalDateTime.class);
            assertThat(sentMsgSentTime, is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
        });
    }
}

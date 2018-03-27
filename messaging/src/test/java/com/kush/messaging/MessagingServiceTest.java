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

        server.beginSession(user1);
        Content content1 = new TextContent("Message 1");
        Destination destination1 = new UserIdBasedDestination(user2.getId());
        messagingService.sendMessage(content1, destination1);
        server.endSession();

        server.beginSession(user2);
        List<Message> recentMessages = messagingService.getRecentMessages(5);
        assertThat(recentMessages, hasSize(1));
        Message message1 = recentMessages.get(0);

        Content receivedContent1 = message1.getContent();
        assertThat(receivedContent1, is(instanceOf(TextContent.class)));
        TextContent receivedTextContent1 = (TextContent) receivedContent1;
        assertThat(receivedTextContent1.getText(), is(equalTo("Message 1")));

        Metadata metadata1 = message1.getMetadata();
        Identifier valueSender1 = metadata1.getValue(MetadataConstants.KEY_SENDER, Identifier.class);
        assertThat(valueSender1, is(equalTo(user1.getId())));
        LocalDateTime valueSentTime1 = metadata1.getValue(MetadataConstants.KEY_SENT_TIME, LocalDateTime.class);
        assertThat(valueSentTime1, is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
        server.endSession();
    }
}

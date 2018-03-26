package com.kush.messaging;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.kush.lib.service.remoting.auth.User;
import com.kush.messaging.services.MessagingService;
import com.kush.service.TestApplicationServer;

public class MessagingServiceTest {

    @Rule
    public TestApplicationServer server = new TestApplicationServer(5);

    private MessagingService messagingService;

    @Before
    public void beforeEachTest() throws Exception {
        messagingService = new MessagingService();
        server.registerService(messagingService);
    }

    @Test
    public void sendMessage() throws Exception {
        User[] users = server.getUsers();
        User user1 = users[0];
        User user2 = users[1];

        server.beginSession(user1);
        server.endSession();

        server.beginSession(user2);
        server.endSession();
    }
}

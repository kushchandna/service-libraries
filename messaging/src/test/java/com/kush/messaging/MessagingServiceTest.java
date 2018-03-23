package com.kush.messaging;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;

public class MessagingServiceTest {

    private MessagingService messagingService;

    @Before
    public void beforeEachTest() throws Exception {
        Context context = ContextBuilder.create().build();
        messagingService = new MessagingService() {

            @Override
            protected Context getContext() {
                return context;
            }
        };
    }

    @Test
    public void test() throws Exception {
        messagingService.toString();
    }
}

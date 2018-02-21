package com.kush.lib.auth;

import com.kush.lib.auth.authentication.Authenticator;
import com.kush.lib.auth.authentication.ThreadBasedAuthenticator;
import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;
import com.kush.lib.service.server.api.ServiceInitializationFailedException;

public class SampleAuthApplication {

    public static void main(String[] args) throws ServiceInitializationFailedException {

        ApplicationServer server = new ApplicationServer();
        server.registerService(SampleAuthHelloService.class);

        Authenticator authenticator = new ThreadBasedAuthenticator();
        Context context = ContextBuilder.create()
            .withInstance(Authenticator.class, authenticator)
            .build();
        server.start(context);
    }
}

package com.kush.lib.auth;

import com.kush.lib.auth.authentication.AuthenticatedServiceRequestResolver;
import com.kush.lib.auth.authentication.Authenticator;
import com.kush.lib.auth.authentication.ThreadBasedAuthenticator;
import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;
import com.kush.lib.service.server.api.ServiceInitializationFailedException;
import com.kush.lib.service.server.api.ServiceRequestResolverFactory;

public class SampleAuthApplication {

    public static void main(String[] args) throws ServiceInitializationFailedException {
        Authenticator authenticator = new ThreadBasedAuthenticator();
        ServiceRequestResolverFactory underlyingResolverFactory = ServiceRequestResolverFactory.DEFAULT;
        ServiceRequestResolverFactory resolverFactory =
                new AuthenticatedServiceRequestResolver.Factory(authenticator, underlyingResolverFactory);
        ApplicationServer server = new ApplicationServer(resolverFactory);
        server.registerService(SampleAuthHelloService.class);

        Context context = ContextBuilder.create()
            .withInstance(Authenticator.class, authenticator)
            .build();
        server.start(context);
    }
}

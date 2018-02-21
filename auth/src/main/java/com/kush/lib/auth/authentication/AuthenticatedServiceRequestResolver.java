package com.kush.lib.auth.authentication;

import com.kush.lib.auth.AuthToken;
import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public class AuthenticatedServiceRequestResolver implements ServiceRequestResolver {

    private final Authenticator authenticator;
    private final ServiceRequestResolver underlyingResolver;

    public AuthenticatedServiceRequestResolver(Authenticator authenticator, ServiceRequestResolver underlyingResolver) {
        this.authenticator = authenticator;
        this.underlyingResolver = underlyingResolver;
    }

    @Override
    public <T> T resolve(ServiceRequest<T> request) throws ServiceRequestFailedException {
        if (!(request instanceof AuthenticatedServiceRequest)) {
            throw new ServiceRequestFailedException("Service request is not authenticated");
        }
        AuthenticatedServiceRequest<T> authServiceRequest = (AuthenticatedServiceRequest<T>) request;
        AuthToken token = authServiceRequest.getToken();
        authenticator.login(token);
        try {
            return underlyingResolver.resolve(request);
        } finally {
            authenticator.logout();
        }
    }
}
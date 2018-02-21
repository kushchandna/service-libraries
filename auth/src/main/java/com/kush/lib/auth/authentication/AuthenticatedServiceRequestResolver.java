package com.kush.lib.auth.authentication;

import com.kush.lib.auth.AuthToken;
import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;
import com.kush.lib.service.server.ServiceInvokerProvider;
import com.kush.lib.service.server.ServiceRequestResolverFactory;

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

    public static class Factory implements ServiceRequestResolverFactory {

        private final Authenticator authenticator;
        private final ServiceRequestResolverFactory underlyingResolverFactory;

        public Factory(Authenticator authenticator, ServiceRequestResolverFactory underlyingResolverFactory) {
            this.authenticator = authenticator;
            this.underlyingResolverFactory = underlyingResolverFactory;
        }

        @Override
        public ServiceRequestResolver create(ServiceInvokerProvider serviceInvokerProvider) {
            ServiceRequestResolver resolver = underlyingResolverFactory.create(serviceInvokerProvider);
            return new AuthenticatedServiceRequestResolver(authenticator, resolver);
        }
    }
}

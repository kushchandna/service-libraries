package com.kush.lib.auth.authentication;

import com.kush.lib.auth.User;
import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public class AuthenticatedServiceRequestResolver implements ServiceRequestResolver {

    private final ServiceRequestResolver underlyingResolver;

    public AuthenticatedServiceRequestResolver(ServiceRequestResolver underlyingResolver) {
        this.underlyingResolver = underlyingResolver;
    }

    @Override
    public <T> T resolve(ServiceRequest request, ReturnType<T> returnType) throws ServiceRequestFailedException {
        if (!(request instanceof AuthenticatedServiceRequest)) {
            return underlyingResolver.resolve(request, returnType);
        }
        AuthenticatedServiceRequest authServiceRequest = (AuthenticatedServiceRequest) request;
        Token token = authServiceRequest.getToken();
        User user = token.getUser();
        user.toString();
        return null;
    }
}

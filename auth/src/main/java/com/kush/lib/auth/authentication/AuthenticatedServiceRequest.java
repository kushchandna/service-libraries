package com.kush.lib.auth.authentication;

import com.kush.lib.auth.AuthToken;
import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestResolver.ReturnType;

public class AuthenticatedServiceRequest<T> extends ServiceRequest<T> {

    private static final long serialVersionUID = 1L;

    private final AuthToken token;

    public AuthenticatedServiceRequest(AuthToken token, String serviceName, String methodName, ReturnType<T> returnType,
            Object[] args) {
        super(serviceName, methodName, returnType, args);
        this.token = token;
    }

    public AuthToken getToken() {
        return token;
    }
}

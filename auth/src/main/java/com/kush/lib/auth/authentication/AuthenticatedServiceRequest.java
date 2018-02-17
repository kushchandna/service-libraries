package com.kush.lib.auth.authentication;

import com.kush.lib.service.remoting.api.ServiceRequest;

public class AuthenticatedServiceRequest extends ServiceRequest {

    private static final long serialVersionUID = 1L;

    private final Token token;

    public AuthenticatedServiceRequest(Token token, String serviceName, String methodName, Object[] args) {
        super(serviceName, methodName, args);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}

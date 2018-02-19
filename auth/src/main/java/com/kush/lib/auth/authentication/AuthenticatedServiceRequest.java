package com.kush.lib.auth.authentication;

import com.kush.lib.auth.AuthToken;
import com.kush.lib.service.remoting.api.ServiceRequest;

public class AuthenticatedServiceRequest extends ServiceRequest {

    private static final long serialVersionUID = 1L;

    private final AuthToken token;

    public AuthenticatedServiceRequest(AuthToken token, String serviceName, String methodName, Object[] args) {
        super(serviceName, methodName, args);
        this.token = token;
    }

    public AuthToken getToken() {
        return token;
    }
}

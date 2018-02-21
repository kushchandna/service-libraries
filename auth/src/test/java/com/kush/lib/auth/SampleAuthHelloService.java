package com.kush.lib.auth;

import com.kush.lib.auth.authentication.Authenticator;
import com.kush.lib.auth.authentication.annotations.AuthenticationRequired;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;

@Service(name = "SampleAuthHelloService")
public class SampleAuthHelloService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod(name = "sayHello")
    public String sayHello() {
        String name = getName();
        return "Hello " + name;
    }

    private String getName() {
        Authenticator authenticator = getContext().getInstance(Authenticator.class);
        User user = authenticator.getCurrentUser();
        return getName(user);
    }

    private String getName(User user) {
        return null;
    }
}

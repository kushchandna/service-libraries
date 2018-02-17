package com.kush.lib.auth;

import com.kush.lib.auth.authentication.annotations.AuthenticationRequired;
import com.kush.lib.auth.authentication.annotations.CallingUserId;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.api.annotations.ServiceMethod;
import com.kush.utils.id.Identifier;

@Service(name = "SampleAuthHelloService")
public class SampleAuthHelloService {

    @AuthenticationRequired
    @ServiceMethod(name = "sayHello")
    public String sayHello(@CallingUserId Identifier userId) {
        String name = getName(userId);
        return "Hello " + name;
    }

    private String getName(Identifier userId) {
        return null;
    }
}

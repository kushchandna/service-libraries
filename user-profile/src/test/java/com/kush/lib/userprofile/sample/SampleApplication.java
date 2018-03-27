package com.kush.lib.userprofile.sample;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.authentication.credential.DefaultUserCredentialPersistor;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.authentication.credential.UserCredentialPersistor;
import com.kush.lib.service.server.local.LocalApplicationServer;
import com.kush.lib.userprofile.UserProfileService;

public class SampleApplication {

    public static void main(String[] args) throws Exception {
        ApplicationServer server = new LocalApplicationServer();
        server.registerService(UserProfileService.class);
        Persistor<UserCredential> userCredentialPersistor = InMemoryPersistor.forType(UserCredential.class);
        DefaultUserCredentialPersistor defaultUserCredentialPersistor =
                new DefaultUserCredentialPersistor(userCredentialPersistor);
        Context context = ContextBuilder.create()
            .withPersistor(UserCredentialPersistor.class, defaultUserCredentialPersistor)
            .build();
        server.start(context);
    }
}

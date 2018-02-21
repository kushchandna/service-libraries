package com.kush.lib.auth.authentication;

import com.kush.lib.auth.AuthToken;
import com.kush.lib.auth.User;

public class ThreadBasedAuthenticator implements Authenticator {

    private final ThreadLocal<AuthToken> CURRENT = new ThreadLocal<>();

    @Override
    public void login(AuthToken token) {
        CURRENT.set(token);
    }

    @Override
    public void logout() {
        CURRENT.remove();
    }

    @Override
    public User getCurrentUser() {
        AuthToken token = CURRENT.get();
        if (token == null) {
            throw new IllegalArgumentException();
        }
        return token.getUser();
    }
}

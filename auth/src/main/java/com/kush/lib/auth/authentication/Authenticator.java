package com.kush.lib.auth.authentication;

import com.kush.lib.auth.AuthToken;
import com.kush.lib.auth.User;

public interface Authenticator {

    void login(AuthToken token);

    void logout();

    User getCurrentUser();
}

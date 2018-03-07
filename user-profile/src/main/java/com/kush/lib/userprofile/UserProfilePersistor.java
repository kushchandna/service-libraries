package com.kush.lib.userprofile;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.User;

public interface UserProfilePersistor extends Persistor<UserProfile> {

    UserProfile getProfileForUser(User user) throws PersistorOperationFailedException;
}

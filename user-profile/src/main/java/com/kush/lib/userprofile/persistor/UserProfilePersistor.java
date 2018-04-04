package com.kush.lib.userprofile.persistor;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.userprofile.profile.UserProfile;

public interface UserProfilePersistor extends Persistor<UserProfile> {

    UserProfile getProfileForUser(User user) throws PersistorOperationFailedException;
}

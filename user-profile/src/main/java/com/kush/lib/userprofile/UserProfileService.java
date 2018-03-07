package com.kush.lib.userprofile;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

@Service(name = "User Profile")
public class UserProfileService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod(name = "Update")
    public UserProfile updateProfile(String name) throws ServiceRequestFailedException {
        User user = getCurrentUser();
        UserProfilePersistor profilePersistor = getUserProfilePersistor();
        UserProfile profile = new UserProfile(user.getId(), name);
        try {
            return profilePersistor.save(profile);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get")
    public UserProfile getProfile() throws ServiceRequestFailedException {
        User user = getCurrentUser();
        UserProfilePersistor profilePersistor = getUserProfilePersistor();
        try {
            return profilePersistor.getProfileForUser(user);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    private UserProfilePersistor getUserProfilePersistor() {
        return getContext().getInstance(UserProfilePersistor.class);
    }
}

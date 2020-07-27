package com.kush.lib.profile.services;

import static com.kush.utils.commons.CollectionUtils.singletonMultiValueMap;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.persistors.ProfilePersister;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;

@Service
public class UserProfileService extends BaseService {

    private final ValueValidator valueValidator;

    public UserProfileService() {
        valueValidator = new ValueValidator();
    }

    @AuthenticationRequired
    @ServiceMethod
    public void updateProfileField(String fieldName, Object value)
            throws ValidationFailedException, PersistorOperationFailedException, NoSuchFieldException {
        User currentUser = getCurrentUser();
        ProfileTemplate template = getInstance(ProfileTemplate.class);
        ProfilePersister profilePersistor = getProfilePersistor();

        Field field = template.getField(fieldName);
        valueValidator.validate(field, value);

        if (field.isNoRepeatitionAllowed()) {
            List<User> matchingUsers = findMatchingUsers(singletonMultiValueMap(fieldName, value));
            if (!matchingUsers.isEmpty()) {
                throw new ValidationFailedException("User with %s '%s' already exists.", field.getDisplayName(), value);
            }
        }

        Profile profile = getOrCreateProfile(profilePersistor, currentUser);
        profilePersistor.updateProfileField(profile.getId(), fieldName, value);
    }

    @AuthenticationRequired
    @ServiceMethod
    public Profile getProfile() throws PersistorOperationFailedException {
        User currentUser = getCurrentUser();
        ProfilePersister profilePersistor = getProfilePersistor();
        return profilePersistor.getProfile(currentUser);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<User> findMatchingUsers(Map<String, Set<Object>> fieldVsValues) throws PersistorOperationFailedException {
        checkSessionActive();
        ProfilePersister profilePersistor = getProfilePersistor();
        List<Profile> profiles = profilePersistor.getMatchingProfiles(fieldVsValues);
        return profiles.stream().map(p -> p.getOwner()).collect(toList());
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(ProfilePersister.class);
        checkContextHasValueFor(ProfileTemplate.class);
    }

    private Profile getOrCreateProfile(ProfilePersister profilePersistor, User user)
            throws PersistorOperationFailedException {
        Profile profile = profilePersistor.getProfile(user);
        if (profile == null) {
            profile = profilePersistor.createProfile(user);
        }
        return profile;
    }

    private ProfilePersister getProfilePersistor() {
        return getInstance(ProfilePersister.class);
    }
}

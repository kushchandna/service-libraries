package com.kush.lib.profile.services;

import static com.google.common.collect.Streams.stream;
import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.ValueValidator;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

@Service
public class UserProfileService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod
    public void updateProfileField(String fieldName, Object value)
            throws ValidationFailedException, PersistorOperationFailedException, NoSuchFieldException {
        Identifier currentUserId = getCurrentUser().getId();
        ProfileTemplate template = getInstance(ProfileTemplate.class);
        ValueValidator valueValidator = getInstance(ValueValidator.class);
        ProfilePersistor profilePersistor = getProfilePersistor();

        Field field = template.getField(fieldName);
        valueValidator.validate(field, value);

        if (field.isNoRepeatitionAllowed()) {
            List<Identifier> matchingUsers = findMatchingUsers(fieldName, value);
            if (!matchingUsers.isEmpty()) {
                throw new ValidationFailedException("User with %s '%s' already exists.", field.getDisplayName(), value);
            }
        }

        Profile profile = getOrCreateProfile(profilePersistor, currentUserId);
        profilePersistor.updateProfileField(profile.getId(), fieldName, value);
    }

    @AuthenticationRequired
    @ServiceMethod
    public Profile getProfile() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ProfilePersistor profilePersistor = getProfilePersistor();
        return profilePersistor.getProfile(currentUserId);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Identifier> findMatchingUsers(String fieldName, Object value) throws PersistorOperationFailedException {
        checkSessionActive();
        ProfilePersistor profilePersistor = getProfilePersistor();
        Iterator<Profile> profiles = profilePersistor.getMatchingProfiles(fieldName, value);
        return stream(profiles).map(p -> p.getOwner()).collect(toList());
    }

    private Profile getOrCreateProfile(ProfilePersistor profilePersistor, Identifier currentUserId)
            throws PersistorOperationFailedException {
        Profile profile = profilePersistor.getProfile(currentUserId);
        if (profile == null) {
            profile = profilePersistor.createProfile(currentUserId);
        }
        return profile;
    }

    private ProfilePersistor getProfilePersistor() {
        return getInstance(ProfilePersistor.class);
    }
}

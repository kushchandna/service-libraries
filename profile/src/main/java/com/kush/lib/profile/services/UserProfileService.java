package com.kush.lib.profile.services;

import static com.google.common.collect.Streams.stream;

import java.util.Iterator;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.ValueValidator;
import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.service.server.BaseService;
import com.kush.utils.id.Identifier;

public class UserProfileService extends BaseService {

    public void updateProfileField(String fieldName, Object value)
            throws ValidationFailedException, PersistorOperationFailedException, NoSuchFieldException {
        Identifier currentUserId = getCurrentUser().getId();
        ProfileTemplate template = getInstance(ProfileTemplate.class);
        ValueValidator valueValidator = getInstance(ValueValidator.class);
        ProfilePersistor profilePersistor = getProfilePersistor();

        Field field = template.getField(fieldName);
        valueValidator.validate(field, value);
        Profile profile = getOrCreateProfile(profilePersistor, currentUserId);

        if (field.isNoRepeatitionAllowed()) {
            Iterator<Identifier> matchingUsers = findMatchingUsers(fieldName, value);
            if (matchingUsers.hasNext()) {
                throw new ValidationFailedException("User with %s '%s' already exists.", field.getDisplayName(), value);
            }
        }
        profilePersistor.updateProfileField(profile.getId(), fieldName, value);
    }

    public Profile getProfile() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ProfilePersistor profilePersistor = getProfilePersistor();
        return profilePersistor.getProfile(currentUserId);
    }

    public Iterator<Identifier> findMatchingUsers(String fieldName, Object value) throws PersistorOperationFailedException {
        checkSessionActive();
        ProfilePersistor profilePersistor = getProfilePersistor();
        Iterator<Profile> profiles = profilePersistor.getMatchingProfiles(fieldName, value);
        return stream(profiles).map(p -> p.getOwner()).iterator();
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

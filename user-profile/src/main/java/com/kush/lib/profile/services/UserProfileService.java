package com.kush.lib.profile.services;

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
            throws ValidationFailedException, PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ProfileTemplate template = getInstance(ProfileTemplate.class);
        ValueValidator valueValidator = getInstance(ValueValidator.class);
        ProfilePersistor profilePersistor = getInstance(ProfilePersistor.class);

        Field field = template.getField(fieldName);
        valueValidator.validate(field, value);

        Profile profile = profilePersistor.getProfile(currentUserId);
        if (profile == null) {
            profile = profilePersistor.createProfile(currentUserId);
        }
        profilePersistor.updateProfileField(profile.getId(), fieldName, value);
    }

    public Profile getProfile() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        ProfilePersistor profilePersistor = getInstance(ProfilePersistor.class);
        return profilePersistor.getProfile(currentUserId);
    }
}

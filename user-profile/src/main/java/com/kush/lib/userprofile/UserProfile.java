package com.kush.lib.userprofile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class UserProfile implements Identifiable {

    private final Identifier profileId;
    private final Identifier userId;
    private final Map<String, Object> profileFields;

    public UserProfile(Identifier userId, Map<String, Object> profileFields) {
        this(Identifier.NULL, userId, profileFields);
    }

    public UserProfile(Identifier profileId, Identifier userId, Map<String, Object> profileFields) {
        this.profileId = profileId;
        this.userId = userId;
        this.profileFields = new HashMap<>(profileFields);
    }

    @Override
    public Identifier getId() {
        return profileId;
    }

    public Identifier getUserId() {
        return userId;
    }

    public <T> T getFieldValue(String key, Class<T> type) {
        return type.cast(profileFields.get(key));
    }

    public Map<String, Object> getAllFields() {
        return Collections.unmodifiableMap(profileFields);
    }
}

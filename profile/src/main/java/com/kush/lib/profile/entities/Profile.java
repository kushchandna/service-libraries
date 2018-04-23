package com.kush.lib.profile.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Profile implements Identifiable {

    private final Identifier profileId;
    private final Identifier owner;
    private final Map<String, Object> fields = new HashMap<>();

    public Profile(Identifier owner, Map<String, Object> fields) {
        this(Identifier.NULL, owner, fields);
    }

    public Profile(Identifier profileId, Profile profile) {
        this(profileId, profile.getOwner(), profile.getFields());
    }

    public Profile(Identifier profileId, Identifier owner, Map<String, Object> fields) {
        this.profileId = profileId;
        this.owner = owner;
        this.fields.putAll(fields);
    }

    @Override
    public Identifier getId() {
        return profileId;
    }

    public Identifier getOwner() {
        return owner;
    }

    public Map<String, Object> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    void updateField(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    @Override
    public String toString() {
        return "Profile [profileId=" + profileId + ", owner=" + owner + ", fields=" + fields + "]";
    }
}

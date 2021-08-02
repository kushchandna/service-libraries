package com.kush.lib.profile.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.annotations.Exportable;

@Exportable
public class Profile implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier profileId;
    private final User owner;
    private final Map<String, Object> fields = new HashMap<>();

    public Profile(User owner, Map<String, Object> fields) {
        this(Identifier.NULL, owner, fields);
    }

    public Profile(Identifier profileId, Profile profile) {
        this(profileId, profile.getOwner(), profile.getFields());
    }

    public Profile(Identifier profileId, User owner, Map<String, Object> fields) {
        this.profileId = profileId;
        this.owner = owner;
        this.fields.putAll(fields);
    }

    @Override
    public Identifier getId() {
        return profileId;
    }

    public User getOwner() {
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

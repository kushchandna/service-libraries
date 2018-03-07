package com.kush.lib.userprofile;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class UserProfile implements Identifiable {

    private final Identifier profileId;
    private final Identifier userId;
    private final String name;

    public UserProfile(Identifier userId, String name) {
        this(Identifier.NULL, userId, name);
    }

    public UserProfile(Identifier profileId, Identifier userId, String name) {
        this.profileId = profileId;
        this.userId = userId;
        this.name = name;
    }

    @Override
    public Identifier getId() {
        return profileId;
    }

    public Identifier getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}

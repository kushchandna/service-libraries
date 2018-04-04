package com.kush.lib.userprofile;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class UserProfile implements Identifiable {

    private final Identifier profileId;

    public UserProfile() {
        this(Identifier.NULL);
    }

    public UserProfile(Identifier profileId, UserProfile userProfile) {
        this(profileId);
    }

    public UserProfile(Identifier profileId) {
        this.profileId = profileId;
    }

    @Override
    public Identifier getId() {
        return profileId;
    }
}

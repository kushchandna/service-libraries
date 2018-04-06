package com.kush.lib.profile.entities;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Profile implements Identifiable {

    private final Identifier profileId;

    public Profile() {
        this(Identifier.NULL);
    }

    public Profile(Identifier profileId, Profile userProfile) {
        this(profileId);
    }

    public Profile(Identifier profileId) {
        this.profileId = profileId;
    }

    @Override
    public Identifier getId() {
        return profileId;
    }
}

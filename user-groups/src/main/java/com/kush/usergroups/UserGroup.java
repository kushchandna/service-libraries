package com.kush.usergroups;

import java.time.LocalDateTime;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class UserGroup implements Identifiable {

    private final Identifier groupId;
    private final String name;
    private final Identifier owner;
    private final LocalDateTime creationDate;

    public UserGroup(String name, Identifier owner, LocalDateTime creationDate) {
        this(Identifier.NULL, name, owner, creationDate);
    }

    public UserGroup(Identifier groupId, UserGroup userGroup) {
        this(groupId, userGroup.getName(), userGroup.getOwner(), userGroup.getCreationDate());
    }

    public UserGroup(Identifier groupId, String name, Identifier owner, LocalDateTime creationDate) {
        this.groupId = groupId;
        this.name = name;
        this.owner = owner;
        this.creationDate = creationDate;
    }

    @Override
    public Identifier getId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Identifier getOwner() {
        return owner;
    }
}

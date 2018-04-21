package com.kush.lib.group.entities;

import java.time.LocalDateTime;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Group implements Identifiable {

    private final Identifier groupId;
    private final String groupName;
    private final Identifier owner;
    private final LocalDateTime createdAt;

    public Group(Identifier groupId, Group group) {
        this(groupId, group.getGroupName(), group.getOwner(), group.getCreatedAt());
    }

    public Group(String groupName, Identifier owner, LocalDateTime createdAt) {
        this(Identifier.NULL, groupName, owner, createdAt);
    }

    public Group(Identifier groupId, String groupName, Identifier owner, LocalDateTime createdAt) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.owner = owner;
        this.createdAt = createdAt;
    }

    @Override
    public Identifier getId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Identifier getOwner() {
        return owner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

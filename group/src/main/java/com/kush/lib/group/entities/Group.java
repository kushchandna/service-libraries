package com.kush.lib.group.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.service.annotations.Exportable;

@Exportable
public class Group implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

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

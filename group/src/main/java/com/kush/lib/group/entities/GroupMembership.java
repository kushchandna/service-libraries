package com.kush.lib.group.entities;

import java.time.LocalDateTime;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class GroupMembership implements Identifiable {

    private final Identifier groupMembershipId;
    private final Identifier groupId;
    private final Identifier member;
    private final LocalDateTime addedAt;

    public GroupMembership(Identifier groupMembershipId, GroupMembership groupMembership) {
        this(groupMembershipId, groupMembership.getGroupId(), groupMembership.getMember(), groupMembership.getAddedAt());
    }

    public GroupMembership(Identifier groupId, Identifier member, LocalDateTime addedAt) {
        this(Identifier.NULL, groupId, member, addedAt);
    }

    public GroupMembership(Identifier groupMembershipId, Identifier groupId, Identifier member, LocalDateTime addedAt) {
        this.groupMembershipId = groupMembershipId;
        this.groupId = groupId;
        this.member = member;
        this.addedAt = addedAt;
    }

    @Override
    public Identifier getId() {
        return groupMembershipId;
    }

    public Identifier getGroupId() {
        return groupId;
    }

    public Identifier getMember() {
        return member;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }
}

package com.kush.usergroups;

import java.time.LocalDateTime;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class UserGroupMember implements Identifiable {

    private final Identifier id;
    private final Identifier groupId;
    private final Identifier userId;
    private final LocalDateTime memberSince;
    private final Identifier addedBy;

    public UserGroupMember(Identifier groupId, Identifier userId, LocalDateTime memberSince, Identifier addedBy) {
        this(Identifier.NULL, groupId, userId, memberSince, addedBy);
    }

    public UserGroupMember(Identifier id, UserGroupMember userGroupMember) {
        this(id, userGroupMember.getGroupId(), userGroupMember.getUserId(), userGroupMember.getMemberSince(),
                userGroupMember.getAddedBy());
    }

    public UserGroupMember(Identifier id, Identifier groupId, Identifier userId, LocalDateTime memberSince, Identifier addedBy) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.memberSince = memberSince;
        this.addedBy = addedBy;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public Identifier getGroupId() {
        return groupId;
    }

    public Identifier getUserId() {
        return userId;
    }

    public LocalDateTime getMemberSince() {
        return memberSince;
    }

    public Identifier getAddedBy() {
        return addedBy;
    }
}

package com.kush.lib.group.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.utils.id.Identifier;

public class UserGroupService extends BaseService {

    public Group createGroup(String groupName) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        LocalDateTime currentDateTime = getCurrentDateTime();
        GroupPersistor groupPersistor = getGroupPersistor();
        Group group = groupPersistor.createGroup(groupName, currentUserId, currentDateTime);
        groupPersistor.addGroupMember(group.getId(), currentUserId, currentDateTime);
        return group;
    }

    public void addMembers(Identifier groupId, Set<Identifier> userIds) {
        checkSessionActive();
        LocalDateTime currentDateTime = getCurrentDateTime();
        GroupPersistor groupPersistor = getGroupPersistor();
        for (Identifier userId : userIds) {
            try {
                groupPersistor.addGroupMember(groupId, userId, currentDateTime);
            } catch (PersistorOperationFailedException e) {
                // notify partial failure
            }
        }
    }

    public List<GroupMembership> getGroupMembers(Identifier groupId)
            throws ServiceRequestFailedException, PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        GroupPersistor groupPersistor = getGroupPersistor();
        List<GroupMembership> groupMembers = groupPersistor.getGroupMembers(groupId);
        boolean isMember = groupMembers.stream().anyMatch(m -> m.getMember().equals(currentUserId));
        if (!isMember) {
            throw new ServiceRequestFailedException("Only members can get group members.");
        }
        return groupMembers;
    }

    public List<Group> getGroups() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        GroupPersistor groupPersistor = getGroupPersistor();
        return groupPersistor.getGroups(currentUserId);
    }

    private GroupPersistor getGroupPersistor() {
        return getInstance(GroupPersistor.class);
    }

    private LocalDateTime getCurrentDateTime() {
        Clock clock = getInstance(Clock.class);
        return LocalDateTime.now(clock);
    }
}

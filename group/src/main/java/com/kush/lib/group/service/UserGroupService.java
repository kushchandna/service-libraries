package com.kush.lib.group.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

@Service
public class UserGroupService extends BaseService {

    private static final Logger LOGGER = LogManager.getFormatterLogger(UserGroupService.class);

    @AuthenticationRequired
    @ServiceMethod
    public Group createGroup(String groupName) throws PersistorOperationFailedException {
        LOGGER.info("Creating group with name %s", groupName);
        Identifier currentUserId = getCurrentUser().getId();
        LocalDateTime currentDateTime = getCurrentDateTime();
        GroupPersister groupPersistor = getGroupPersistor();
        Group group = groupPersistor.createGroup(groupName, currentUserId, currentDateTime);
        LOGGER.info("Created group [%s]. Now adding self as member.", group.getId(), group.getGroupName());
        groupPersistor.addGroupMember(group.getId(), currentUserId, currentDateTime);
        LOGGER.info("Added self as group member");
        return group;
    }

    @AuthenticationRequired
    @ServiceMethod
    public void removeGroup(Identifier groupId) throws PersistorOperationFailedException, ServiceRequestFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        GroupPersister groupPersistor = getGroupPersistor();
        Group group = groupPersistor.getGroup(groupId);
        Identifier owner = group.getOwner();
        if (!currentUserId.equals(owner)) {
            throw new ServiceRequestFailedException("Only owner can remove a group");
        }
        groupPersistor.removeGroup(groupId);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void addMembers(Identifier groupId, Set<Identifier> userIds) {
        checkSessionActive();
        LocalDateTime currentDateTime = getCurrentDateTime();
        GroupPersister groupPersistor = getGroupPersistor();
        for (Identifier userId : userIds) {
            try {
                groupPersistor.addGroupMember(groupId, userId, currentDateTime);
                LOGGER.info("Added user %s to group %s", userId, groupId);
            } catch (PersistorOperationFailedException e) {
                // notify partial failure
                LOGGER.error("Could not add user %s to group %s. Reason: %s", userId, groupId, e.getMessage());
                LOGGER.error(e);
            }
        }
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<GroupMembership> getGroupMembers(Identifier groupId)
            throws PersistorOperationFailedException, ValidationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        GroupPersister groupPersistor = getGroupPersistor();
        List<GroupMembership> groupMembers = groupPersistor.getGroupMembers(groupId);
        boolean isMember = groupMembers.stream().anyMatch(m -> m.getMember().equals(currentUserId));
        if (!isMember) {
            throw new ValidationFailedException("Only members can get group members.");
        }
        return groupMembers;
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Group> getGroups() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        GroupPersister groupPersistor = getGroupPersistor();
        return groupPersistor.getGroups(currentUserId);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(GroupPersister.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
    }

    private GroupPersister getGroupPersistor() {
        return getInstance(GroupPersister.class);
    }

    private LocalDateTime getCurrentDateTime() {
        Clock clock = getInstance(Clock.class);
        return LocalDateTime.now(clock);
    }
}

package com.kush.lib.group.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

@Service(name = "User Group")
public class UserGroupService extends BaseService {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(UserGroupService.class);

    @AuthenticationRequired
    @ServiceMethod(name = "Create Group")
    public Group createGroup(String groupName) throws PersistorOperationFailedException {
        LOGGER.info("Creating group with name %s", groupName);
        Identifier currentUserId = getCurrentUser().getId();
        LocalDateTime currentDateTime = getCurrentDateTime();
        GroupPersistor groupPersistor = getGroupPersistor();
        Group group = groupPersistor.createGroup(groupName, currentUserId, currentDateTime);
        LOGGER.info("Created group [%s]. Now adding self as member.", group.getId(), group.getGroupName());
        groupPersistor.addGroupMember(group.getId(), currentUserId, currentDateTime);
        LOGGER.info("Added self as group member");
        return group;
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Add Members")
    public void addMembers(Identifier groupId, Set<Identifier> userIds) {
        checkSessionActive();
        LocalDateTime currentDateTime = getCurrentDateTime();
        GroupPersistor groupPersistor = getGroupPersistor();
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
    @ServiceMethod(name = "Get Group Members")
    public List<GroupMembership> getGroupMembers(Identifier groupId)
            throws PersistorOperationFailedException, ValidationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        GroupPersistor groupPersistor = getGroupPersistor();
        List<GroupMembership> groupMembers = groupPersistor.getGroupMembers(groupId);
        boolean isMember = groupMembers.stream().anyMatch(m -> m.getMember().equals(currentUserId));
        if (!isMember) {
            throw new ValidationFailedException("Only members can get group members.");
        }
        return groupMembers;
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get Groups")
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

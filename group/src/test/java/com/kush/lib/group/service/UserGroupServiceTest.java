package com.kush.lib.group.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.group.entities.DefaultGroupPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.helpers.InMemoryPersister;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseServiceTest;

public class UserGroupServiceTest extends BaseServiceTest {

    private UserGroupService userGroupService;

    @Before
    public void beforeEachTest() throws Exception {
        Persister<Group> delegateGroupPersistor = InMemoryPersister.forType(Group.class);
        Persister<GroupMembership> delegateGroupMembershipPersistor = InMemoryPersister.forType(GroupMembership.class);
        addToContext(GroupPersister.class, new DefaultGroupPersistor(delegateGroupPersistor, delegateGroupMembershipPersistor));
        userGroupService = registerService(UserGroupService.class);
    }

    @Test
    public void createEmptyUserGroup() throws Exception {
        String testGroupName = "Test Group";

        User user = user(0);
        runAuthenticatedOperation(() -> {
            Group group = userGroupService.createGroup(testGroupName);
            assertThat(group.getId(), is(notNullValue()));
            assertThat(group.getGroupName(), is(equalTo(testGroupName)));
            assertThat(group.getOwner(), is(equalTo(user.getId())));
            assertThat(group.getCreatedAt(), is(equalTo(getCurrentDateTime())));

            List<GroupMembership> members = userGroupService.getGroupMembers(group.getId());
            assertThat(members, hasSize(1));
            GroupMembership selfMembership = members.get(0);
            assertThat(selfMembership.getId(), is(notNullValue()));
            assertThat(selfMembership.getGroupId(), is(equalTo(group.getId())));
            assertThat(selfMembership.getMember(), is(equalTo(user.getId())));
            assertThat(selfMembership.getAddedAt(), is(equalTo(getCurrentDateTime())));
        });
    }
}

package com.kush.usergroups;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.service.TestApplicationEnvironment;
import com.kush.utils.id.Identifier;

public class UserGroupServiceTest {

    private static final Instant CURRENT_TIME = Instant.now();
    private static final ZoneId CURRENT_ZONE = ZoneId.systemDefault();
    private static final Clock CLOCK = Clock.fixed(CURRENT_TIME, CURRENT_ZONE);

    @Rule
    public TestApplicationEnvironment testEnv = new TestApplicationEnvironment() {

        @Override
        protected ContextBuilder createContextBuilder() {
            Persistor<UserGroup> delegate = InMemoryPersistor.forType(UserGroup.class);
            Persistor<UserGroupMember> memberPersistor = InMemoryPersistor.forType(UserGroupMember.class);
            return ContextBuilder.create()
                .withInstance(Clock.class, CLOCK)
                .withInstance(UserGroupPersistor.class, new DefaultUserGroupPersistor(delegate, memberPersistor));
        };
    };
    private UserGroupService userGroupService;

    @Before
    public void beforeEachTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        userGroupService = new UserGroupService();
        testEnv.registerService(userGroupService);
    }

    @Test
    public void createFirstGroup() throws Exception {
        String testGroupName = "Test Group";
        User testUser = testEnv.getTestUser();
        testEnv.runAuthenticatedOperation(testUser, () -> {
            UserGroup group = userGroupService.createGroup(testGroupName);
            assertThat(group.getId(), is(not(equalTo(Identifier.NULL))));
            assertThat(group.getName(), is(equalTo(testGroupName)));
            assertThat(group.getCreationDate(), is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
            assertThat(group.getOwner(), is(equalTo(testUser.getId())));

            List<UserGroupMember> groupMembers = userGroupService.getGroupMembers(group.getId());
            assertThat(groupMembers, hasSize(1));
            UserGroupMember onlyGroupMember = groupMembers.get(0);
            assertGroupMember(onlyGroupMember, group, testUser, testUser);

            UserGroup savedGroup = userGroupService.getGroup(group.getId());
            assertThat(savedGroup.getId(), is(equalTo(group.getId())));
            assertThat(savedGroup.getName(), is(equalTo(testGroupName)));
            assertThat(savedGroup.getCreationDate(), is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
            assertThat(savedGroup.getOwner(), is(equalTo(testUser.getId())));
        });
    }

    @Test
    public void addMembersToGroup() throws Exception {
        User[] users = testEnv.getUsers();
        User user1 = users[0];
        User user2 = users[1];
        User user3 = users[2];

        testEnv.runAuthenticatedOperation(users[0], () -> {
            UserGroup group = userGroupService.createGroup("Test Group");

            List<UserGroupMember> groupMembers = userGroupService.getGroupMembers(group.getId());
            assertThat(groupMembers, hasSize(1));
            UserGroupMember memberG1U1 = groupMembers.get(0);
            assertGroupMember(memberG1U1, group, user1, user1);

            UserGroupMember memberG1U2 = userGroupService.addMemberToGroup(group.getId(), user2.getId());
            assertGroupMember(memberG1U2, group, user2, user1);

            UserGroupMember memberG1U3 = userGroupService.addMemberToGroup(group.getId(), user3.getId());
            assertGroupMember(memberG1U3, group, user3, user1);

            List<UserGroupMember> updatedGroupMembers = userGroupService.getGroupMembers(group.getId());
            assertThat(updatedGroupMembers, hasSize(3));
            assertGroupMember(updatedGroupMembers.get(0), group, user1, user1);
            assertGroupMember(updatedGroupMembers.get(1), group, user2, user1);
            assertGroupMember(updatedGroupMembers.get(2), group, user3, user1);
        });
    }

    private void assertGroupMember(UserGroupMember member, UserGroup group, User memberUser, User addedBy) {
        assertThat(member.getId(), is(not(equalTo(Identifier.NULL))));
        assertThat(member.getGroupId(), is(equalTo(group.getId())));
        assertThat(member.getUserId(), is(equalTo(memberUser.getId())));
        assertThat(member.getMemberSince(), is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
        assertThat(member.getAddedBy(), is(equalTo(addedBy.getId())));
    }
}

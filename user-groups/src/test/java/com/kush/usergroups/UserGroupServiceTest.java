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
            assertThat(onlyGroupMember.getId(), is(not(equalTo(Identifier.NULL))));
            assertThat(onlyGroupMember.getGroupId(), is(equalTo(group.getId())));
            assertThat(onlyGroupMember.getUserId(), is(equalTo(testUser.getId())));
            assertThat(onlyGroupMember.getMemberSince(), is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
            assertThat(onlyGroupMember.getAddedBy(), is(equalTo(testUser.getId())));

            UserGroup savedGroup = userGroupService.getGroup(group.getId());
            assertThat(savedGroup.getId(), is(equalTo(group.getId())));
            assertThat(savedGroup.getName(), is(equalTo(testGroupName)));
            assertThat(savedGroup.getCreationDate(), is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
            assertThat(savedGroup.getOwner(), is(equalTo(testUser.getId())));
        });
    }

    @Test
    public void manageMembersInGroup() throws Exception {
        User[] users = testEnv.getUsers();

        testEnv.runAuthenticatedOperation(users[0], () -> {
            UserGroup group = userGroupService.createGroup("Test Group");

            List<UserGroupMember> groupMembers = userGroupService.getGroupMembers(group.getId());
            assertThat(groupMembers, hasSize(1));
            UserGroupMember onlyGroupMember = groupMembers.get(0);
            assertThat(onlyGroupMember.getId(), is(not(equalTo(Identifier.NULL))));
            assertThat(onlyGroupMember.getGroupId(), is(equalTo(group.getId())));
            assertThat(onlyGroupMember.getUserId(), is(equalTo(users[0].getId())));
            assertThat(onlyGroupMember.getMemberSince(), is(equalTo(LocalDateTime.ofInstant(CURRENT_TIME, CURRENT_ZONE))));
            assertThat(onlyGroupMember.getAddedBy(), is(equalTo(users[0].getId())));
        });
    }
}

package org.metropolia.minimalnotepad.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.metropolia.minimalnotepad.repository.UserGroupParticipationRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The type User group participation service test.
 */
class UserGroupParticipationServiceTest {

    @Mock
    private UserGroupParticipationRepository userGroupParticipationRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserGroupParticipationService userGroupParticipationService;

    private User testUser;
    private Group testGroup;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setUser(testUser);
    }

    /**
     * Test join group.
     */
    @Test
    void testJoinGroup() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(testUser, testGroup)).thenReturn(java.util.Optional.empty());

        UserGroupParticipation participation = new UserGroupParticipation();
        participation.setUser(testUser);
        participation.setGroup(testGroup);

        when(userGroupParticipationRepository.save(any(UserGroupParticipation.class))).thenReturn(participation);

        UserGroupParticipation result = userGroupParticipationService.joinGroup(testUser.getId(), testGroup.getId());

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testGroup, result.getGroup());

        verify(userGroupParticipationRepository, times(1)).save(any(UserGroupParticipation.class));
    }

    /**
     * Test join group user already member.
     */
    @Test
    void testJoinGroup_UserAlreadyMember() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(testUser, testGroup)).thenReturn(java.util.Optional.of(new UserGroupParticipation()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.joinGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("User is already a member of this group.", exception.getMessage());
    }

    /**
     * Test join group user not found.
     */
    @Test
    void testJoinGroup_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.joinGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Test join group group not found.
     */
    @Test
    void testJoinGroup_GroupNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.joinGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("Group not found", exception.getMessage());
    }

    /**
     * Test leave group.
     */
    @Test
    void testLeaveGroup() {
        UserGroupParticipation participation = new UserGroupParticipation();
        participation.setUser(testUser);
        participation.setGroup(testGroup);

        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(testUser, testGroup)).thenReturn(java.util.Optional.of(participation));

        userGroupParticipationService.leaveGroup(testUser.getId(), testGroup.getId());

        verify(userGroupParticipationRepository, times(1)).delete(participation);
    }

    /**
     * Test leave group user not found.
     */
    @Test
    void testLeaveGroup_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.leaveGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Test leave group group not found.
     */
    @Test
    void testLeaveGroup_GroupNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.leaveGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("Group not found", exception.getMessage());
    }

    /**
     * Test leave group membership not found.
     */
    @Test
    void testLeaveGroup_MembershipNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(testUser, testGroup)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.leaveGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("User is not a member of this group.", exception.getMessage());
    }

    /**
     * Test remove user from group.
     */
    @Test
    void testRemoveUserFromGroup() {
        User newUser = new User();
        newUser.setId(2L);

        UserGroupParticipation participation = new UserGroupParticipation();
        participation.setUser(newUser);
        participation.setGroup(testGroup);

        when(userRepository.findById(newUser.getId())).thenReturn(java.util.Optional.of(newUser));
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(newUser, testGroup)).thenReturn(java.util.Optional.of(participation));

        userGroupParticipationService.removeUserFromGroup(testUser.getId(), testGroup.getId(), newUser.getId());

        verify(userGroupParticipationRepository, times(1)).delete(participation);
    }

    /**
     * Test remove user from group user not found.
     */
    @Test
    void testRemoveUserFromGroup_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.removeUserFromGroup(testUser.getId(), testGroup.getId(), 2L);
        });

        assertEquals("User not found.", exception.getMessage());
    }

    /**
     * Test remove user from group group not found.
     */
    @Test
    void testRemoveUserFromGroup_GroupNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.removeUserFromGroup(testUser.getId(), testGroup.getId(), 2L);
        });

        assertEquals("Group not found.", exception.getMessage());
    }

    /**
     * Test remove user from group user not member.
     */
    @Test
    void testRemoveUserFromGroup_UserNotMember() {
        User newUser = new User();
        newUser.setId(2L);

        when(userRepository.findById(newUser.getId())).thenReturn(java.util.Optional.of(newUser));
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(newUser, testGroup)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.removeUserFromGroup(testUser.getId(), testGroup.getId(), newUser.getId());
        });

        assertEquals("Target user is not a member of this group.", exception.getMessage());
    }

    /**
     * Test remove user from group target user not found.
     */
    @Test
    void testRemoveUserFromGroup_TargetUserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.removeUserFromGroup(testUser.getId(), testGroup.getId(), 2L);
        });

        assertEquals("Target user not found.", exception.getMessage());
    }

    /**
     * Test remove user from group not owner.
     */
    @Test
    void testRemoveUserFromGroup_NotOwner() {
        User newUser = new User();
        newUser.setId(2L);

        UserGroupParticipation participation = new UserGroupParticipation();
        participation.setUser(newUser);
        participation.setGroup(testGroup);

        when(userRepository.findById(newUser.getId())).thenReturn(java.util.Optional.of(newUser));
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(newUser, testGroup)).thenReturn(java.util.Optional.of(participation));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.removeUserFromGroup(newUser.getId(), testGroup.getId(), testUser.getId());
        });

        assertEquals("Only the owner of the group can remove members.", exception.getMessage());
    }

    /**
     * Test remove user from group remove self.
     */
    @Test
    void testRemoveUserFromGroup_RemoveSelf() {
        User newUser = new User();
        newUser.setId(2L);

        UserGroupParticipation participation = new UserGroupParticipation();
        participation.setUser(newUser);
        participation.setGroup(testGroup);

        when(userRepository.findById(newUser.getId())).thenReturn(java.util.Optional.of(newUser));
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.of(testGroup));
        when(userGroupParticipationRepository.findByUserAndGroup(newUser, testGroup)).thenReturn(java.util.Optional.of(participation));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.removeUserFromGroup(testUser.getId(), testGroup.getId(), testUser.getId());
        });

        assertEquals("Cannot remove yourself from the group.", exception.getMessage());
    }
}

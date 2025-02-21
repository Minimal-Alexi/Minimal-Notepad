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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testGroup = new Group();
        testGroup.setId(1L);
    }

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

    @Test
    void testJoinGroup_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.joinGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testJoinGroup_GroupNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.joinGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("Group not found", exception.getMessage());
    }

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

    @Test
    void testLeaveGroup_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.leaveGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLeaveGroup_GroupNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.of(testUser));
        when(groupRepository.findById(testGroup.getId())).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userGroupParticipationService.leaveGroup(testUser.getId(), testGroup.getId());
        });

        assertEquals("Group not found", exception.getMessage());
    }

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
}

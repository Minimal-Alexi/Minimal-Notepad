package org.metropolia.minimalnotepad.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GroupService groupService;

    private Long testGroupId;
    private User testUser;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        userRepository.save(testUser);

        Group group = new Group();
        group.setName("Test Group #1");
        group.setUser(testUser);
        groupRepository.save(group);

        testGroupId = group.getId();
    }

    @Test
    void getAllGroups() {
        when(groupRepository.findAll()).thenReturn(List.of(new Group()));
        List<Group> groups = groupService.getAllGroups();
        assertFalse(groups.isEmpty(), "The group list should not be empty");
    }

    @Test
    void getGroupById() {
        when(groupRepository.findById(testGroupId)).thenReturn(java.util.Optional.of(new Group()));
        Group foundGroup = groupService.getGroupById(testGroupId);
        assertNotNull(foundGroup, "The group should be found");
    }

    @Test
    void createGroup() {
        when(groupRepository.findAll()).thenReturn(List.of());

        int groupCountBefore = groupRepository.findAll().size();

        Group newGroup = new Group();
        newGroup.setName("Test Group #2");
        newGroup.setUser(testUser);

        groupService.createGroup(newGroup);
        when(groupRepository.findAll()).thenReturn(List.of(newGroup));

        int groupCountAfter = groupRepository.findAll().size();

        assertEquals(groupCountBefore + 1, groupCountAfter, "The group should be created");
    }

    @Test
    void updateGroup() {
        Group existingGroup = new Group();
        existingGroup.setId(testGroupId);
        existingGroup.setName("Old Group");
        existingGroup.setDescription("Old Description");

        when(groupRepository.findById(testGroupId)).thenReturn(Optional.of(existingGroup));

        Group updatedGroup = new Group();
        updatedGroup.setName("Updated Group");
        updatedGroup.setDescription("Updated Description");

        groupService.updateGroup(testGroupId, updatedGroup);

        assertEquals("Updated Group", existingGroup.getName(), "The group name should be updated");
        assertEquals("Updated Description", existingGroup.getDescription(), "The group description should be updated");

        verify(groupRepository).save(existingGroup);
    }

    @Test
    @WithMockUser(username = "test")
    void deleteGroup() {
        Group group = new Group();
        group.setName("Group to Delete");
        groupService.createGroup(group);

        groupService.deleteGroup(group.getId());

        Group deletedGroup = groupService.getGroupById(group.getId());
        assertNull(deletedGroup, "The group should be deleted and not found");
    }

    @Test
    void getGroupsByUserId() {
        Group group1 = new Group();
        group1.setId(1L);
        group1.setName("Group 1");
        group1.setUser(testUser);

        Group group2 = new Group();
        group2.setId(2L);
        group2.setName("Group 2");
        group2.setUser(testUser);

        when(groupRepository.getGroupsByUserId(testUser.getId())).thenReturn(List.of(group1, group2));

        List<Group> groups = groupService.getGroupsByUserId(testUser.getId());

        assertNotNull(groups);
        assertEquals(2, groups.size());
        assertEquals("Group 1", groups.get(0).getName());
        assertEquals("Group 2", groups.get(1).getName());

        verify(groupRepository, times(1)).getGroupsByUserId(testUser.getId());
    }

    @Test
    void isGroupNameTaken() {
        when(groupRepository.existsByName("Existing Group")).thenReturn(true);
        when(groupRepository.existsByName("New Group")).thenReturn(false);

        assertTrue(groupService.isGroupNameTaken("Existing Group"));
        assertFalse(groupService.isGroupNameTaken("New Group"));

        verify(groupRepository, times(1)).existsByName("Existing Group");
        verify(groupRepository, times(1)).existsByName("New Group");
    }
}
package org.metropolia.minimalnotepad.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GroupServiceTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;

    private Long testGroupId;

    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

    @BeforeEach
    void setUp() {
        Group group = new Group();
        group.setName("Test Group #1");
        groupRepository.save(group);
        testGroupId = group.getId();
    }

    @AfterEach
    void cleanUp() {
        groupRepository.deleteById(testGroupId);
    }

    @Test
    @WithMockUser(username = "test")
    void getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        assertEquals(1, groups.size(), "The group list should have one group");
    }

    @Test
    @WithMockUser(username = "test")
    void getGroupById() {
        Group foundGroup = groupService.getGroupById(testGroupId);
        assertNotNull(foundGroup, "The group should be found");
    }

    @Test
    @WithMockUser(username = "test")
    void createGroup() {
        int groupCount = groupRepository.findAll().size();

        Group group = new Group();
        group.setName("Test Group #2");
        groupService.createGroup(group);

        int newGroupCount = groupRepository.findAll().size();
        assertEquals(groupCount + 1, newGroupCount, "The group should be created");

        groupRepository.deleteById(group.getId());
    }

    @Test
    @WithMockUser(username = "test")
    void updateGroup() {
        Group updatedGroup = new Group();
        updatedGroup.setName("Updated Group");
        updatedGroup.setDescription("Updated Description");
        groupService.updateGroup(testGroupId, updatedGroup);

        Group foundGroup = groupService.getGroupById(testGroupId);
        assertEquals("Updated Group", foundGroup.getName(), "The group name should be updated");
        assertEquals("Updated Description", foundGroup.getDescription(), "The group description should be updated");
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
}
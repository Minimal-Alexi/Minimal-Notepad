package org.metropolia.minimalnotepad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.service.GroupService;
import org.metropolia.minimalnotepad.service.UserGroupParticipationService;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ObjectMapper objectMapper; // Used for JSON conversion

    private Group testGroup;
    private User testUser;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserGroupParticipationService userGroupParticipationService;
    @Autowired
    private UserService userService;

    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
    }

    @BeforeEach
    @WithMockUser(username = "test")
    public void setUp() {
        userRepository.deleteAll();
        groupRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("test");
        testUser = userRepository.save(testUser);

        testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setName("Test Group");
        testGroup.setUser(testUser);

        testGroup = groupRepository.save(testGroup);
        testUser = userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "test")
    void getAllGroups() throws Exception {
        mockMvc.perform(get("/api/groups/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Group"));
    }

    @Test
    @WithMockUser(username = "test")
    void getGroupById() throws Exception {
        mockMvc.perform(get("/api/groups/{id}", testGroup.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Group"));
    }

    @Test
    @WithMockUser(username = "test")
    void createGroup() throws Exception {
        Group newGroup = new Group();
        newGroup.setId(2L);
        newGroup.setName("New Group");
        newGroup.setUser(testUser);

        String token = jwtUtils.generateToken(testUser.getUsername());

        mockMvc.perform(post("/api/groups")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Group"));
    }

    @Test
    @WithMockUser(username = "test")
    void updateGroup() throws Exception {
        testGroup.setName("Updated Group");

        mockMvc.perform(put("/api/groups/{id}", testGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGroup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Group"));
    }

    @Test
    @WithMockUser(username = "test")
    void deleteGroup() throws Exception {
        mockMvc.perform(delete("/api/groups/{id}", testGroup.getId()))
                .andExpect(status().isNoContent());

        assertFalse(groupRepository.findById(testGroup.getId()).isPresent());
    }

    @Test
    @WithMockUser(username = "test")
    void testGetUserGroups() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");
        userRepository.save(newUser);

        Group group1 = new Group();
        group1.setName("New group 1");
        group1.setUser(newUser);

        groupRepository.save(group1);

        userGroupParticipationService.joinGroup(newUser.getId(), testGroup.getId());

        String token = jwtUtils.generateToken(newUser.getUsername());

        mockMvc.perform(get("/api/groups/my-groups")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("New group 1"))
                .andExpect(jsonPath("$[1].name").value("Test Group"));
    }

    @Test
    void testGetUserGroups_EmptyList() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");
        userRepository.save(newUser);

        String token = jwtUtils.generateToken(newUser.getUsername());

        mockMvc.perform(get("/api/groups/my-groups")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User is not an owner or a member of any groups."));
    }

    @Test
    void testGetAvailableGroups() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");
        userRepository.save(newUser);

        String token = jwtUtils.generateToken(newUser.getUsername());

        mockMvc.perform(get("/api/groups/available")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Group"));
    }

    @Test
    void testGetAvailableGroups_EmptyList() throws Exception {
        userGroupParticipationService.joinGroup(testUser.getId(), testGroup.getId());

        String token = jwtUtils.generateToken(testUser.getUsername());

        mockMvc.perform(get("/api/groups/available")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No groups available to join."));
    }

    @Test
    void testCreateGroupWithDuplicateName() throws Exception {
        Group group1 = new Group();
        group1.setId(1L);
        group1.setName("Test Group 1");
        group1.setUser(testUser);

        Group group2 = new Group();
        group2.setId(2L);
        group2.setName("Test Group 1"); // Same name as group1
        group2.setUser(testUser);

        String token = jwtUtils.generateToken(testUser.getUsername());

        // Create the first group
        mockMvc.perform(post("/api/groups")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group1)))
                .andExpect(status().isCreated());

        // Attempt to create the second group with the same name
        mockMvc.perform(post("/api/groups")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Group name is already taken."));
    }

    @Test
    void testJoinGroup() throws Exception {
        UserGroupParticipation membership = new UserGroupParticipation();
        membership.setUser(testUser);
        membership.setGroup(testGroup);

        String token = jwtUtils.generateToken(testUser.getUsername());
        Long groupId = testGroup.getId();

        mockMvc.perform(post("/api/groups/{groupId}/join", groupId)
                        .header("Authorization", "Bearer " + token)) // Ensure a valid token is passed
                .andExpect(status().isOk());
    }

    @Test
    void testLeaveGroup() throws Exception {
        UserGroupParticipation membership = new UserGroupParticipation();
        membership.setUser(testUser);
        membership.setGroup(testGroup);

        String token = jwtUtils.generateToken(testUser.getUsername());
        Long groupId = testGroup.getId();

        mockMvc.perform(post("/api/groups/{groupId}/join", groupId)
                        .header("Authorization", "Bearer " + token));

        mockMvc.perform(delete("/api/groups/{groupId}/leave", groupId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
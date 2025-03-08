package org.metropolia.minimalnotepad.controller;

import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.dto.GroupDetailedDTO;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.metropolia.minimalnotepad.service.GroupService;

import org.metropolia.minimalnotepad.service.UserGroupParticipationService;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final UserService userService;
    private final GroupService groupService;
    private final JwtUtils jwtUtils;
    private final UserGroupParticipationService userGroupParticipationService;

    public GroupController(UserService userService, GroupService groupService, JwtUtils jwtUtils, UserGroupParticipationService userGroupParticipationService) {
        this.userService = userService;
        this.groupService = groupService;
        this.jwtUtils = jwtUtils;
        this.userGroupParticipationService = userGroupParticipationService;
    }

    @GetMapping("/all")
    public List<GroupDetailedDTO> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return GroupDetailedDTO.fromGroups(groups);
    }

    // Get all groups that the user is a member of (created + joined)
    @GetMapping("/my-groups")
    public ResponseEntity<?> getUserGroups(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtUtils.getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);

        List<Group> userGroups = groupService.getUserGroups(user);

        if (userGroups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User is not an owner or a member of any groups."));
        }
        return ResponseEntity.ok(GroupDetailedDTO.fromGroups(userGroups));
    }

    // Get all groups that the user can join (all groups - user's groups)
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableGroups(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtUtils.getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);

        List<Group> groups = groupService.getAvailableGroups(user);

        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "No groups available to join."));
        }
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public GroupDetailedDTO getGroupById(@PathVariable Long id) {
        return new GroupDetailedDTO(groupService.getGroupById(id));
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Group group) {
        String token = jwtUtils.getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);

        // Check if the group name is unique
        if (groupService.isGroupNameTaken(group.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Group name is already taken."));
        }

        group.setUser(user);
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity
                .created(URI.create("/api/groups/" + createdGroup.getId()))
                .body(createdGroup);
    }

    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable Long id, @RequestBody Group updatedGroup) {
        return groupService.updateGroup(id, updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    // User joins a group
    @PostMapping("/{groupId}/join")
    public ResponseEntity<?> joinGroup(@RequestHeader("Authorization") String authorizationHeader,
                                       @PathVariable Long groupId) {
        String token = jwtUtils.getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);

        UserGroupParticipation membership = userGroupParticipationService.joinGroup(user.getId(), groupId);
        return ResponseEntity.ok(membership);
    }

    // User leaves a group
    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<?> leaveGroup(@RequestHeader("Authorization") String authorizationHeader,
                                        @PathVariable Long groupId) {
        String token = jwtUtils.getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);

        userGroupParticipationService.leaveGroup(user.getId(), groupId);
        return ResponseEntity.noContent().build();
    }

    // Remove user from group
    @DeleteMapping("/{groupId}/remove/{userId}")
    public ResponseEntity<?> removeUserFromGroup(@RequestHeader("Authorization") String authorizationHeader,
                                                 @PathVariable Long groupId, @PathVariable Long userId) {
        String token = jwtUtils.getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);

        userGroupParticipationService.removeUserFromGroup(user.getId(), groupId, userId);
        return ResponseEntity.noContent().build();
    }
}


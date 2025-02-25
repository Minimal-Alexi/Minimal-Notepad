package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.ResourceDoesntExistException;
import org.metropolia.minimalnotepad.exception.UserDoesntOwnResourceException;
import org.metropolia.minimalnotepad.exception.UserNotFoundException;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupId;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.metropolia.minimalnotepad.repository.UserGroupParticipationRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserGroupParticipationService {

    private final UserGroupParticipationRepository userGroupParticipationRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public UserGroupParticipationService(UserGroupParticipationRepository userGroupParticipationRepository,
                                         GroupRepository groupRepository,
                                         UserRepository userRepository) {
        this.userGroupParticipationRepository = userGroupParticipationRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    // Join a group
    public UserGroupParticipation joinGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Check if user is already a member
        if (userGroupParticipationRepository.findByUserAndGroup(user, group).isPresent()) {
            throw new RuntimeException("User is already a member of this group.");
        }

        UserGroupId id = new UserGroupId(user.getId(), group.getId());
        UserGroupParticipation participation = new UserGroupParticipation();
        participation.setId(id);
        participation.setUser(user);
        participation.setGroup(group);
        return userGroupParticipationRepository.save(participation);
    }

    // Leave a group
    public void leaveGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        UserGroupParticipation membership = userGroupParticipationRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new RuntimeException("User is not a member of this group."));

        userGroupParticipationRepository.delete(membership);
    }

    // Remove user from group
    public void removeUserFromGroup(Long ownerId, Long groupId, Long targetUserId) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceDoesntExistException("Group not found."));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("Target user not found."));

        // Check if the requester is the owner of the group
        if (group.getUser().getId() != ownerId) {
            throw new UserDoesntOwnResourceException("Only the owner of the group can remove members.");
        }

        if (group.getUser().getId() == targetUser.getId()) {
            throw new RuntimeException("Cannot remove yourself from the group.");
        }

        UserGroupParticipation targetMembership = userGroupParticipationRepository.findByUserAndGroup(targetUser, group)
                .orElseThrow(() -> new ResourceDoesntExistException("Target user is not a member of this group."));

        userGroupParticipationRepository.delete(targetMembership);
    }
}
package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.metropolia.minimalnotepad.model.Group;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Group service.
 */
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * Instantiates a new Group service.
     *
     * @param groupRepository the group repository
     */
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Gets all groups.
     *
     * @return the all groups
     */
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    /**
     * Gets user groups.
     *
     * @param user the user
     * @return the user groups
     */
    public List<Group> getUserGroups(User user) {
        List<Group> userCreatedGroups = user.getGroups();
        List<Group> userJoinedGroups = new ArrayList<>();
        List<UserGroupParticipation> userParticipations = user.getGroupParticipationsList();
        for (UserGroupParticipation userGroupParticipation : userParticipations) {
            userJoinedGroups.add(userGroupParticipation.getGroup());
        }
        userCreatedGroups.addAll(userJoinedGroups);

        return userCreatedGroups;
    }

    /**
     * Gets available groups.
     *
     * @param user the user
     * @return the available groups
     */
    public List<Group> getAvailableGroups(User user) {
        List<Group> allGroups = groupRepository.findAll();
        List<Group> userGroups = getUserGroups(user);
        allGroups.removeAll(userGroups);

        return allGroups;
    }

    /**
     * Gets group by id.
     *
     * @param id the id
     * @return the group by id
     */
    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null); // Using orElse on Optional<Group>
    }

    /**
     * Create group group.
     *
     * @param group the group
     * @return the group
     */
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    /**
     * Update group group.
     *
     * @param id           the id
     * @param updatedGroup the updated group
     * @return the group
     */
    public Group updateGroup(Long id, Group updatedGroup) {
        Group group = getGroupById(id);
        group.setName(updatedGroup.getName());
        group.setDescription(updatedGroup.getDescription());
        return groupRepository.save(group);
    }

    /**
     * Delete group.
     *
     * @param id the id
     */
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    /**
     * Is group name taken boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isGroupNameTaken(String name) {
        return groupRepository.existsByName(name);
    }
}

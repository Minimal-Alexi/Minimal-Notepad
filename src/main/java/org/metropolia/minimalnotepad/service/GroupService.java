package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.metropolia.minimalnotepad.model.Group;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> getUserGroups(User user) {
        List<Group> userCreatedGroups = user.getGroups();
        List<Group> userJoinedGroups = new ArrayList<>();
        List<UserGroupParticipation> userParticipations = user.getGroupParticipationsList();
        for(UserGroupParticipation userGroupParticipation : userParticipations){
            userJoinedGroups.add(userGroupParticipation.getGroup());
        }
        userCreatedGroups.addAll(userJoinedGroups);

        return userCreatedGroups;
    }

    public List<Group> getAvailableGroups(User user) {
        List<Group> allGroups = groupRepository.findAll();
        List<Group> userGroups = getUserGroups(user);
        allGroups.removeAll(userGroups);

        return allGroups;
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null); // Using orElse on Optional<Group>
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, Group updatedGroup) {
        Group group = getGroupById(id);
        group.setName(updatedGroup.getName());
        group.setDescription(updatedGroup.getDescription());
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    public boolean isGroupNameTaken(String name) {
        return groupRepository.existsByName(name);
    }
}

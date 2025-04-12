package org.metropolia.minimalnotepad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailedDTO {
    private Long id;
    private String name;
    private String description;
    @JsonIgnoreProperties({"notes", "password", "groupParticipationsList", "enabled", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "groups"})
    private User owner;
    private List<UserGroupParticipationDTO> userGroupParticipationsList;

    public GroupDetailedDTO(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.owner = group.getUser();
        this.userGroupParticipationsList = new ArrayList<>();
        for (UserGroupParticipation participation : group.getUserGroupParticipationsList()) {
            this.userGroupParticipationsList.add(new UserGroupParticipationDTO(participation));  // Add DTO for each participation
        }
    }

    public static List<GroupDetailedDTO> fromGroups(List<Group> groups) {
        List<GroupDetailedDTO> groupDetailedDTOList = new ArrayList<>();
        for (Group group : groups) {
            groupDetailedDTOList.add(new GroupDetailedDTO(group));
        }
        return groupDetailedDTOList;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public User getOwner() {
        return owner;
    }
    public List<UserGroupParticipationDTO> getUserGroupParticipationsList() {
        return userGroupParticipationsList;
    }
}

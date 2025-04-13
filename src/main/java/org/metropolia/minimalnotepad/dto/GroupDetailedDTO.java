package org.metropolia.minimalnotepad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Group detailed dto.
 */
public class GroupDetailedDTO {
    private Long id;
    private String name;
    private String description;
    @JsonIgnoreProperties({"notes", "password", "groupParticipationsList", "enabled", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "groups"})
    private User owner;
    private List<UserGroupParticipationDTO> userGroupParticipationsList;

    /**
     * Instantiates a new Group detailed dto.
     *
     * @param group the group
     */
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

    /**
     * From groups list.
     *
     * @param groups the groups
     * @return the list
     */
    public static List<GroupDetailedDTO> fromGroups(List<Group> groups) {
        List<GroupDetailedDTO> groupDetailedDTOList = new ArrayList<>();
        for (Group group : groups) {
            groupDetailedDTOList.add(new GroupDetailedDTO(group));
        }
        return groupDetailedDTOList;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Gets user group participations list.
     *
     * @return the user group participations list
     */
    public List<UserGroupParticipationDTO> getUserGroupParticipationsList() {
        return userGroupParticipationsList;
    }
}

package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * The type User group participation.
 */
@Entity
@Table(name = "user_groups")
public class UserGroupParticipation {
    @EmbeddedId
    private UserGroupId id = new UserGroupId();
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    @JsonBackReference("participation-user")
    private User user;
    @ManyToOne
    @MapsId("group_id")
    @JoinColumn(name = "group_id")
    @JsonBackReference("participation-group")
    private Group group;

    /**
     * Instantiates a new User group participation.
     */
    public UserGroupParticipation() {

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public UserGroupId getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(UserGroupId id) {
        this.id = id;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets group.
     *
     * @param group the group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public Group getGroup() {
        return group;
    }
}

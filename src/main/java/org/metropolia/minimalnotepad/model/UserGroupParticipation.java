package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "user_groups")
public class UserGroupParticipation {
    @EmbeddedId
    private UserGroupId id = new UserGroupId();
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name="user_id")
    @JsonBackReference("participation-user")
    public User user;
    @ManyToOne
    @MapsId("group_id")
    @JoinColumn(name = "group_id")
    @JsonBackReference("participation-group")
    public Group group;

    public UserGroupParticipation() {

    }

    public UserGroupId getId() { return id; }
    public void setId(UserGroupId id) { this.id = id; }
    public void setUser(User user) {
        this.user = user;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public User getUser() {
        return user;
    }
    public Group getGroup() {
        return group;
    }
}

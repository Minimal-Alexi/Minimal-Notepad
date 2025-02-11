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
    public boolean is_owner;
    public UserGroupParticipation() {

    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public boolean is_owner() {
        return is_owner;
    }
    public void set_owner(boolean owner) {
        is_owner = owner;
    }
    public User getUser() {
        return user;
    }
    public Group getGroup() {
        return group;
    }
}

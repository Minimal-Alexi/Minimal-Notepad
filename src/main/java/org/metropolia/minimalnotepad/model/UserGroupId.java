package org.metropolia.minimalnotepad.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type User group id.
 */
@Embeddable
public class UserGroupId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "group_id")
    private Long groupId;

    /**
     * Instantiates a new User group id.
     */
    public UserGroupId() { }

    /**
     * Instantiates a new User group id.
     *
     * @param userId  the user id
     * @param groupId the group id
     */
    public UserGroupId(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets group id.
     *
     * @return the group id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Sets group id.
     *
     * @param groupId the group id
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserGroupId that = (UserGroupId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}

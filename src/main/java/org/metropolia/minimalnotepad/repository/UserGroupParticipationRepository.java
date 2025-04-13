package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface User group participation repository.
 */
public interface UserGroupParticipationRepository extends JpaRepository<UserGroupParticipation, Long> {
    /**
     * Find by user and group optional.
     *
     * @param user  the user
     * @param group the group
     * @return the optional
     */
    Optional<UserGroupParticipation> findByUserAndGroup(User user, Group group);
}

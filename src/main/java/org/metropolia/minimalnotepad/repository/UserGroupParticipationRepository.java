package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.model.UserGroupParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupParticipationRepository extends JpaRepository<UserGroupParticipation, Long> {
    Optional<UserGroupParticipation> findByUserAndGroup(User user, Group group);
}

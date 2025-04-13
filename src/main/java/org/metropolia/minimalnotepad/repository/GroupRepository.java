package org.metropolia.minimalnotepad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.metropolia.minimalnotepad.model.Group;
import org.springframework.stereotype.Repository;

/**
 * The interface Group repository.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    /**
     * Exists by name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existsByName(String name);
}

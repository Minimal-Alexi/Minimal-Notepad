package org.metropolia.minimalnotepad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.metropolia.minimalnotepad.model.Group;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);
}

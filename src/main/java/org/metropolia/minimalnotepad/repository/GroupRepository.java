package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}

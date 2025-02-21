package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> getGroupsByUserId(long userId);
    boolean existsByName(String name);
}

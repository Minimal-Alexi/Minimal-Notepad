package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();
    Optional<Group> findById(Long id);
    Group save(Group group);
    void deleteById(Long id);
}

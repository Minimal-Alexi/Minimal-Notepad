package org.metropolia.minimalnotepad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.metropolia.minimalnotepad.model.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /* This line is important, but causes errors
    @Query("SELECT g FROM Group g WHERE g.user.id = ?1")*/
    List<Group> findOwnAndJoinedGroupsByUserId(Long userId);

    boolean existsByName(String name);
}

package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username user.
     *
     * @param username the username
     * @return the user
     */
    User findUserByUsername(String username);

    /**
     * Find user by email user.
     *
     * @param email the email
     * @return the user
     */
    User findUserByEmail(String email);
}

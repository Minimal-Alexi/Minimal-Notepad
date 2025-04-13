package org.metropolia.minimalnotepad.dto;


import org.metropolia.minimalnotepad.model.UserGroupParticipation;

/**
 * The type User group participation dto.
 */
public class UserGroupParticipationDTO {
    private Long id;
    private String username;
    private String email;

    /**
     * Instantiates a new User group participation dto.
     *
     * @param participation the participation
     */
    public UserGroupParticipationDTO(UserGroupParticipation participation) {
        this.id = participation.getUser().getId();
        this.username = participation.getUser().getUsername();
        this.email = participation.getUser().getEmail();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }
}

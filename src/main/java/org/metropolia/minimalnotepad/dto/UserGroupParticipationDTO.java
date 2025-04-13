package org.metropolia.minimalnotepad.dto;


import org.metropolia.minimalnotepad.model.UserGroupParticipation;

public class UserGroupParticipationDTO {
    private Long id;
    private String username;
    private String email;

    public UserGroupParticipationDTO(UserGroupParticipation participation) {
        this.id = participation.getUser().getId();
        this.username = participation.getUser().getUsername();
        this.email = participation.getUser().getEmail();
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
}

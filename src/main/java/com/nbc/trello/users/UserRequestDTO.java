package com.nbc.trello.users;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequestDTO {
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;
}

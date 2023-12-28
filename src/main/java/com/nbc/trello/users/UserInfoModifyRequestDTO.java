package com.nbc.trello.users;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserInfoModifyRequestDTO {
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;
}

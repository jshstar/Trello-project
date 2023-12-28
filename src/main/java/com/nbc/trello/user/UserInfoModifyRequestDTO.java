package com.nbc.trello.user;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserInfoModifyRequestDTO {
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;
    private String email;
    private String introduce;
}

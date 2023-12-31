package com.nbc.trello.users.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPWModifyRequestDto {
    private String beforePassword;
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String afterPassword;
}

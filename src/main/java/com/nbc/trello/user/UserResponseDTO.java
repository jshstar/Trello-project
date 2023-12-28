package com.nbc.trello.user;

import com.nbc.trello.CommonResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDTO extends CommonResponseDTO {
    private String username;
    private boolean isAdmin;
}
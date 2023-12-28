package com.nbc.trello.admin;

import com.nbc.trello.user.User;
import com.nbc.trello.user.UserRepository;
import com.nbc.trello.user.UserResponseDTO;
import com.nbc.trello.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    public List<UserResponseDTO> getUserList() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userList = new ArrayList<>();
        users.forEach((user) -> userList.add(new UserResponseDTO(user.getUsername(), user.getRole() == UserRoleEnum.ROLE_ADMIN)));
        return userList;
    }
}
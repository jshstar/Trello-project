package com.nbc.trello.admin;

import com.nbc.trello.user.User;
import com.nbc.trello.user.UserDetailsImpl;
import com.nbc.trello.user.UserResponseDTO;
import com.nbc.trello.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public void checkRole(User user) throws AuthenticationException {
        if(user.getRole() == UserRoleEnum.ROLE_USER){
            throw new AuthenticationException("관리자가 아닙니다.");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/userlist")
    public ResponseEntity<List<UserResponseDTO>> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails) throws AuthenticationException {
        checkRole(userDetails.getUser());
        List<UserResponseDTO> userList = adminService.getUserList();
        return ResponseEntity.ok().body(userList);
    }
}

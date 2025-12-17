package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.query.UsersFilterRequest;
import dev.parhamziaei.teahub.dto.response.user.admin.UserDetailAdminResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
public class UserManageController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(@ModelAttribute UsersFilterRequest filter) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getAllUsers(filter),
                HttpStatus.OK
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getProfile(
                        userId,
                        UserDetailAdminResponse.class
                ),
                HttpStatus.OK
        );
    }

}

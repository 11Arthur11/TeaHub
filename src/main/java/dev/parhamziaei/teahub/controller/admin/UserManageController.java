package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.query.UsersFilterRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.UserDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.UserEditAdminRequest;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
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
    private final MessageService messageService;

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

    @PostMapping("/{userId}/lock")
    public ResponseEntity<SimpleResponse> lockUser(@PathVariable Long userId) {
        userService.userLocked(userId, true);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @PostMapping("/{userId}/unlock")
    public ResponseEntity<SimpleResponse> unlockUser(@PathVariable Long userId) {
        userService.userLocked(userId, false);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getRoles(),
                HttpStatus.OK
        );
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<SimpleResponse> setUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.setRole(userId, roleId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @PostMapping("/{userId}/edit")
    public ResponseEntity<SimpleResponse> editUser(@PathVariable Long userId, @RequestBody UserEditAdminRequest editRequest) {
        userService.editUser(userId, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

}

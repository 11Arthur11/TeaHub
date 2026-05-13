package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.query.UsersFilterRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.RoleListResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.UserDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.UserEditAdminRequest;
import dev.parhamziaei.teahub.dto.response.user.admin.UserListResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
public class UserManageController {

    private final UserService userService;
    private final MessageService messageService;

    @Operation(
            summary = "Getting All Users",
            description = "Returns a paginated list of all users",
            tags = {"User Manage (Admin)"}
    )
    @GetMapping
    public ResponseEntity<DataResponse<PagedModel<UserListResponse>>> getAllUsers(@ModelAttribute UsersFilterRequest filter) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getAllUsers(filter),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Getting User Details",
            description = "Returns a detailed response of specified user",
            tags = {"User Manage (Admin)"}
    )
    @GetMapping("/{userId}")
    public ResponseEntity<DataResponse<UserDetailAdminResponse>> getUserById(@PathVariable Long userId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getProfile(
                        userId,
                        UserDetailAdminResponse.class
                ),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Lock User",
            description = "Lock user and prevent them to login",
            tags = {"User Manage (Admin)"}
    )
    @PostMapping("/{userId}/lock")
    public ResponseEntity<SimpleResponse> lockUser(@PathVariable Long userId) {
        userService.userLocked(userId, true);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Unlock User",
            description = "Unlocks locked user",
            tags = {"User Manage (Admin)"}
    )
    @PostMapping("/{userId}/unlock")
    public ResponseEntity<SimpleResponse> unlockUser(@PathVariable Long userId) {
        userService.userLocked(userId, false);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Roles List",
            description = "Returns a list of all roles",
            tags = {"User Manage (Admin)"}
    )
    @GetMapping("/roles")
    public ResponseEntity<DataResponse<List<RoleListResponse>>> getRoles() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getRoles(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Set User Role",
            description = "Set a role to user",
            tags = {"User Manage (Admin)"}
    )
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<SimpleResponse> setUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.setRole(userId, roleId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Edit User",
            description = "Edits user details, ignores null values",
            tags = {"User Manage (Admin)"}
    )
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

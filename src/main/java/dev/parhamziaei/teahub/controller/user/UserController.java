package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.user.user.UserDetailResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUser currentUser;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<DataResponse<UserDetailResponse>> getProfile() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getProfile(
                        currentUser.getId(),
                        UserDetailResponse.class
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/identity")
    public ResponseEntity<DataResponse<Map<String, String>>> getIdentity() {

        Map<String, String> map = new HashMap<>();
        map.put("roleIdentifier", userService.getProfile(
                currentUser.getId(),
                UserDetailResponse.class
        ).getRole().nameWithoutPrefix());
        map.put("id", currentUser.getId().toString());

        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                map,
                HttpStatus.OK
        );
    }



}

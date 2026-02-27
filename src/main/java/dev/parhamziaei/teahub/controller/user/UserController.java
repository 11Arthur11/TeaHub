package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.payment.user.BalanceChargeRequest;
import dev.parhamziaei.teahub.dto.request.query.WalletTransactionFilterRequest;
import dev.parhamziaei.teahub.dto.response.user.user.UserDetailResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.WalletService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUser currentUser;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getProfile() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                userService.getProfile(
                        currentUser.getId(),
                        UserDetailResponse.class
                ),
                HttpStatus.OK
        );
    }


}

package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.payment.admin.WalletTransactionAdminRequest;
import dev.parhamziaei.teahub.dto.request.payment.user.BalanceChargeRequest;
import dev.parhamziaei.teahub.dto.request.query.WalletTransactionFilterRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.DetailedDataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.user.WalletTransactionResponse;
import dev.parhamziaei.teahub.dto.response.user.user.WalletOverviewResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.InvoiceService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.WalletService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/wallets")
@Tag(name = "Wallet (Admin)")
public class WalletAdminController {

    private final WalletService walletService;
    private final MessageService messageService;

    @Operation(
            summary = "Target User wallet overview"
    )
    @GetMapping("/{userId}/overview")
    public ResponseEntity<DataResponse<WalletOverviewResponse>> getBalance(@PathVariable Long userId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                walletService.getOverview(userId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Creating a transaction for user"
    )
    @PostMapping("/{userId}/transactions")
    public ResponseEntity<SimpleResponse> adjust(
            @PathVariable Long userId,
            @RequestBody WalletTransactionAdminRequest request
    ) {
        walletService.transactionByAdmin(userId, request);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "This is main api for receiving wallet transactions for admin"
    )
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<DataResponse<PagedModel<WalletTransactionResponse>>> getWalletTransactions(
            @PathVariable Long userId,
            @ModelAttribute WalletTransactionFilterRequest filter
    ) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                walletService.getWalletTransactionsByUserId(userId, filter),
                HttpStatus.OK
        );
    }

}
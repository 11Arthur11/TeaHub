package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.payment.user.BalanceChargeRequest;
import dev.parhamziaei.teahub.dto.request.query.WalletTransactionFilterRequest;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.DetailedDataResponse;
import dev.parhamziaei.teahub.dto.response.user.WalletTransactionResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.InvoiceService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.WalletService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/wallet")
public class WalletController {

    private final MessageService messageService;
    private final WalletService walletService;
    private final CurrentUser currentUser;
    private final InvoiceService invoiceService;

    @Operation(
            summary = "User balance",
            tags = {"Wallet"}
    )
    @GetMapping("/balance")
    public ResponseEntity<DataResponse<BigDecimal>> getBalance() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                walletService.getBalance(currentUser.getId()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Redirect user to /v1/invoices/{invoiceToken} - invoice Token will send as data with response",
            tags = {"Wallet"}
    )
    @PostMapping("/charge")
    public ResponseEntity<DetailedDataResponse<Map<String, String>>> chargeWallet(@RequestBody BalanceChargeRequest request) {
        String invoiceToken = invoiceService.createChargeWalletInvoice(
                currentUser.getId(),
                request.getAmount()
        );
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                messageService.get(ServiceMessage.PAYMENT_INVOICE_CREATED),
                Map.of("invoiceToken", invoiceToken),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "This is main api for receiving wallet transactions",
            description = "use cases for example: related resources transactions in resource detail page - " +
                    "Filter schema is WalletTransactionFilterRequest",
            tags = {"Wallet"}
    )
    @GetMapping("/transactions")
    public ResponseEntity<DataResponse<PagedModel<WalletTransactionResponse>>> getWalletTransactions(
            @ModelAttribute WalletTransactionFilterRequest filter
    ) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                walletService.getWalletTransactions(currentUser.getWalletId(), filter),
                HttpStatus.OK
        );
    }

}

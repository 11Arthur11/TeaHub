package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.payment.user.BalanceChargeRequest;
import dev.parhamziaei.teahub.dto.request.query.WalletTransactionFilterRequest;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.WalletService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/wallet")
public class WalletController {

    private final PaymentService paymentService;
    private final MessageService messageService;
    private final WalletService walletService;
    private final CurrentUser currentUser;

    @Operation(summary = "Redirect user to /v1/invoices/{invoiceToken} - invoice Token will send as data with response")
    @PostMapping("/charge")
    public ResponseEntity<?> chargeWallet(@RequestBody BalanceChargeRequest request) {
        String invoiceToken = paymentService.createChargeWalletInvoice(
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

    @GetMapping("/transactions")
    public ResponseEntity<?> getWalletTransactions(@RequestParam WalletTransactionFilterRequest filter) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                walletService.getWalletTransactions(currentUser.getWalletId(), filter),
                HttpStatus.OK
        );
    }

}

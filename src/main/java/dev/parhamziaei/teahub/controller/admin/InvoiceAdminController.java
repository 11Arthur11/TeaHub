package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.payment.admin.AdminDebtInvoiceRequest;
import dev.parhamziaei.teahub.dto.request.query.InvoiceAdminFilterRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.DetailedDataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.payment.admin.InvoiceAdminResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.InvoiceService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/invoices")
@Tag(name = "Invoices (Admin) WORKING ON IT...")
@RequiredArgsConstructor
public class InvoiceAdminController {

    private final InvoiceService invoiceService;
    private final CurrentUser currentUser;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<DataResponse<PagedModel<InvoiceAdminResponse>>> getAllInvoices(InvoiceAdminFilterRequest filterRequest) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                invoiceService.getAllInvoices(filterRequest),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DetailedDataResponse<String>> sendDebtInvoice(@RequestBody AdminDebtInvoiceRequest request) {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.PAYMENT_INVOICE_CREATED),
                invoiceService.createAdminDebtInvoice(currentUser.getId(), request),
                HttpStatus.OK
        );
    }

}

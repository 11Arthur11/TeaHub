package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.query.InvoiceFilterRequest;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.InvoiceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

//    private final CurrentUser currentUser;
//    private final InvoiceService invoiceService;
//
//    @GetMapping
//    public ResponseEntity<?> getInvoices(@ModelAttribute InvoiceFilterRequest request) {
//        return ResponseBuilder.buildSuccess(
//                ResponseType.DATA,
//                invoiceService.getByUserId(
//                        PageRequest.of(
//                                request.getPage(),
//                                request.getSize()),
//                        currentUser.getId(),
//                        request.getStatus()
//                ),
//                HttpStatus.OK
//        );
//    }
//
//    @GetMapping("/{invoiceToken}")
//    public ResponseEntity<?> getInvoice(@PathVariable("invoiceToken") String invoiceToken) {
//        return ResponseBuilder.buildSuccess(
//                ResponseType.DATA,
//                invoiceService.getOwnInvoice(
//                        currentUser.getId(),
//                        invoiceToken
//                ),
//                HttpStatus.OK
//        );
//    }

}

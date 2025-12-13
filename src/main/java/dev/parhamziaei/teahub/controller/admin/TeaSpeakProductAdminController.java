package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/products/teaspeak")
@RequiredArgsConstructor
public class TeaSpeakProductAdminController {

    private final ProductService productService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAllTeaSpeakProducts(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<SimpleResponse> initTeaSpeakProduct(@Valid @RequestBody TeaSpeakProductRequest initRequest) {
        productService.initTeaSpeakProduct(initRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_CREATED),
                HttpStatus.OK
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getTeaSpeakProductById(productId),
                HttpStatus.OK
        );
    }

    @PostMapping("/edit")
    public ResponseEntity<SimpleResponse> updateProduct(@Valid @RequestBody TeaSpeakProductUpdateRequest updateRequest) {
        productService.updateTeaSpeakProduct(updateRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                HttpStatus.OK
        );
    }

    @GetMapping("/{productId}/{enabled}")
    public ResponseEntity<?> changeEnabled(
            @PathVariable Long productId,
            @PathVariable boolean enabled
    ) {
        productService.changeEnabled(productId, enabled);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<SimpleResponse> deleteProduct(@PathVariable Long productId) {
        productService.removeTeaSpeakProduct(productId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_DELETED),
                HttpStatus.OK
        );
    }

}

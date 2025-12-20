package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
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
@RequestMapping("/v1/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAllProducts(),
                HttpStatus.OK
        );
    }

    @PostMapping("/add")
    public ResponseEntity<SimpleResponse> addProduct(@Valid @RequestBody AbstractProductInitRequest initRequest) {
        productService.addProduct(initRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_CREATED),
                HttpStatus.OK
        );
    }

    @PostMapping("/{productId}/edit")
    public ResponseEntity<SimpleResponse> editProduct(@PathVariable Long productId, @Valid @RequestBody AbstractProductEditRequest editRequest) {
        productService.updateProduct(productId, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                HttpStatus.OK
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getProduct(productId),
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
        productService.removeProduct(productId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_DELETED),
                HttpStatus.OK
        );
    }

}

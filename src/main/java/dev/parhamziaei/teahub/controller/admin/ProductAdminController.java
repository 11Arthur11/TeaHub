package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Get all products",
            description = "Returns a list of all products. This endpoint does not support pagination.",
            tags = {"Products (Admin)"}
    )
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAllProducts(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Add new product",
            description = "Creates a new product with initial configuration and makes it available in the system.",
            tags = {"Products (Admin)"}
    )
    @PostMapping("/add")
    public ResponseEntity<SimpleResponse> addProduct(@Valid @RequestBody AbstractProductInitRequest initRequest) {
        productService.addProduct(initRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_CREATED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Edit product",
            description = "Updates the configuration and details of an existing product identified by productId.",
            tags = {"Products (Admin)"}
    )
    @PostMapping("/{productId}/edit")
    public ResponseEntity<SimpleResponse> editProduct(@PathVariable Long productId, @Valid @RequestBody AbstractProductEditRequest editRequest) {
        productService.updateProduct(productId, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get product details",
            description = "Returns detailed information of a specific product identified by productId.",
            tags = {"Products (Admin)"}
    )
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getProduct(productId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Enable or disable product",
            description = "Changes the enabled status of a product. If enabled is true, the product becomes active; otherwise, it will be disabled.",
            tags = {"Products (Admin)"}
    )
    @PatchMapping("/{productId}/{enabled}")
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

    @Operation(
            summary = "Delete product",
            description = "Removes a product permanently from the system.",
            tags = {"Products (Admin)"}
    )
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

package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductDetailResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful response",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "type": "DATA",
                                              "data": [
                                                {
                                                  "id": 1,
                                                  "productName": "Test TeaSpeak Product",
                                                  "enabled": true,
                                                  "price": {
                                                    "amount": 50000,
                                                    "currency": "IRT"
                                                  },
                                                  "period": "روزانه",
                                                  "productType": "TEASPEAK_PRODUCT",
                                                  "maxClients": 32
                                                },
                                                {
                                                  "id": 1,
                                                  "productName": "Test AudioBot Product",
                                                  "enabled": true,
                                                  "providerNodeId": 0,
                                                  "price": {
                                                    "amount": 50000,
                                                    "currency": "IRT"
                                                  },
                                                  "period": "ماهانه",
                                                  "productType": "AUDIO_BOT_PRODUCT"
                                                }
                                              ]
                                            }
                                    """
                            )
                    )
            )
    })
    @GetMapping
    public
    ResponseEntity<DataResponse<List<AbstractProductListResponse>>> getAllProducts() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAllProducts(),
                HttpStatus.OK
        );
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AbstractNewResourceRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "teaspeak-product",
                                    summary = "Add TeaSpeak Product",
                                    value = """
                                            {
                                              "type": "TEASPEAK",
                                              "productName": "TeaSpeak Server 32 Slot",
                                              "categoryId": 1,
                                              "price": 5000,
                                              "enabled": true,
                                              "productPeriod": "MONTHLY",
                                              "maxClients": 32
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "audio-bot-product",
                                    summary = "Add AudioBot Product (providerNodeId is Optional)",
                                    value = """
                                            {
                                              "type": "AUDIO_BOT",
                                              "productName": "Audio Bot Hourly",
                                              "categoryId": 1,
                                              "price": 1000,
                                              "enabled": true,
                                              "productPeriod": "HOURLY",
                                              "providerNodeId": 1
                                            }
                                            """
                            )
                    }
            )
    )
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful response",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "type": "DATA",
                                              "data": {
                                                "id": 1,
                                                "categoryName": "Test Category",
                                                "categorySlug": "test",
                                                "productName": "Test TeaSpeak Product",
                                                "period": "روزانه",
                                                "expiration": "24h",
                                                "orderedResources": 1,
                                                "price": {
                                                  "amount": 50000,
                                                  "currency": "IRT"
                                                },
                                                "enabled": true,
                                                "maxClients": 32
                                              }
                                            }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful response",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "type": "DATA",
                                              "data": {
                                                "id": 2,
                                                "categoryName": "Test Category",
                                                "categorySlug": "test",
                                                "productName": "string",
                                                "period": "ساعتی",
                                                "expiration": "1h",
                                                "orderedResources": 0,
                                                "price": {
                                                  "amount": 50000,
                                                  "currency": "IRT"
                                                },
                                                "enabled": true,
                                                "providerNodeId": null
                                              }
                                            }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/{productId}")
    public ResponseEntity<DataResponse<AbstractProductDetailResponse>> getProduct(@PathVariable Long productId) {
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
    public ResponseEntity<SimpleResponse> changeEnabled(
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

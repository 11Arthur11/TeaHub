package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get All Products")
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
                                                  "price": {
                                                    "amount": 50000,
                                                    "currency": "IRT"
                                                  },
                                                  "period": "روزانه",
                                                  "productType": "TEASPEAK",
                                                  "maxClients": 32
                                                },
                                                {
                                                  "id": 1,
                                                  "productName": "Test AudioBot Product",
                                                  "price": {
                                                    "amount": 50000,
                                                    "currency": "IRT"
                                                  },
                                                  "period": "ماهانه",
                                                  "productType": "AUDIO_BOT"
                                                }
                                              ]
                                            }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/{categorySlug}")
    public ResponseEntity<?> getProductByCategorySlug(@PathVariable String categorySlug) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAvailableProductsByCategorySlug(categorySlug),
                HttpStatus.OK
        );
    }

}

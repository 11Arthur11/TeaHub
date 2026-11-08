package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.dto.request.query.ProductFilterRequest;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                                  "maxClients": 32,
                                                  "presentation": {
                                                      "description": "سرور TeaSpeak با کیفیت بالا و منابع اختصاصی",
                                                      "features": "[{\\"text\\":\\"صدای با کیفیت\\",\\"enabled\\":true},{\\"text\\":\\"بکاپ خودکار\\",\\"enabled\\":true},{\\"text\\":\\"Anti-DDoS Protection\\",\\"enabled\\":false}]",
                                                      "badges": "[{\\"text\\":\\"پرفروش\\",\\"variant\\":\\"success\\"},{\\"text\\":\\"جدید\\",\\"variant\\":\\"primary\\"}]"
                                                  }
                                                },
                                                {
                                                  "id": 1,
                                                  "productName": "Test AudioBot Product",
                                                  "price": {
                                                    "amount": 50000,
                                                    "currency": "IRT"
                                                  },
                                                  "period": "ماهانه",
                                                  "productType": "AUDIO_BOT",
                                                  "presentation": {
                                                      "description": "سرور TeaSpeak با کیفیت بالا و منابع اختصاصی",
                                                      "features": "[{\\"text\\":\\"صدای با کیفیت\\",\\"enabled\\":true},{\\"text\\":\\"بکاپ خودکار\\",\\"enabled\\":true},{\\"text\\":\\"Anti-DDoS Protection\\",\\"enabled\\":false}]",
                                                      "badges": "[{\\"text\\":\\"پرفروش\\",\\"variant\\":\\"success\\"},{\\"text\\":\\"جدید\\",\\"variant\\":\\"primary\\"}]"
                                                  }
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

    @GetMapping
    public ResponseEntity<?> getProductByFilter(@ModelAttribute @Valid ProductFilterRequest filterRequest) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAvailableProductsByFilter(filterRequest),
                HttpStatus.OK
        );
    }

}

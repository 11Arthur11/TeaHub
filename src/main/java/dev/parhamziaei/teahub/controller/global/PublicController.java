package dev.parhamziaei.teahub.controller.global;

import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.CategoryListResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.CategoryService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/global")
public class PublicController {

    private final ProductService productService;
    private final CategoryService categoryService;

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
    @GetMapping("/products/{categorySlug}")
    public ResponseEntity<?> getProductByCategorySlug(@PathVariable String categorySlug) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAvailableProductsByCategorySlug(categorySlug),
                HttpStatus.OK
        );
    }

    @GetMapping("/categories")
    public ResponseEntity<DataResponse<List<CategoryListResponse>>> getCategories() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                categoryService.getAvailableCategories(),
                HttpStatus.OK
        );
    }

}

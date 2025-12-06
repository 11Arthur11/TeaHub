package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
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

    @GetMapping("/{categorySlug}")
    public ResponseEntity<?> getProductByCategorySlug(@PathVariable String categorySlug) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                productService.getAvailableProductsByCategorySlug(categorySlug),
                HttpStatus.OK
        );
    }

}

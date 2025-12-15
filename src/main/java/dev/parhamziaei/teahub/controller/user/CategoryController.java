package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.CategoryService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategories() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                categoryService.getAvailableCategories(),
                HttpStatus.OK
        );
    }

}

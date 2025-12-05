package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.shop.admin.CategoryAdminRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.CategoryService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                categoryService.getAllCategories(),
                HttpStatus.OK
        );
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editCategory(
            @Valid @RequestBody CategoryAdminRequest categoryAdminRequest,
            @RequestParam Long categoryId
    ) {
        categoryService.updateCategory(categoryId, categoryAdminRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                HttpStatus.OK
        );
    }


    @PostMapping()
    public ResponseEntity<SimpleResponse> addCategory(@Valid @RequestBody CategoryAdminRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_CREATED),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestParam Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_DELETED),
                HttpStatus.OK
        );
    }

}

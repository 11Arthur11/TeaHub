package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.shop.admin.CategoryAdminRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.CategoryListAdminResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.CategoryService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;
    private final MessageService messageService;

    @Operation(
            summary = "Get all categories for admin",
            description = "Returns a list of all categories without pagination",
            tags = {"Categories (Admin)"}
    )
    @GetMapping
    public ResponseEntity<DataResponse<List<CategoryListAdminResponse>>> getAllCategories() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                categoryService.getAllCategories(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Edit category",
            description = "Updates an existing category using the provided request body. " +
                    "The category is identified by its ID, and all editable fields will be updated accordingly.",
            tags = {"Categories (Admin)"}
    )
    @PostMapping("/edit/{categoryId}")
    public ResponseEntity<SimpleResponse> editCategory(
            @Valid @RequestBody CategoryAdminRequest categoryAdminRequest,
            @PathVariable Long categoryId
    ) {
        categoryService.updateCategory(categoryId, categoryAdminRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Create new category",
            description = "Creates a new category using the provided request body. " +
                    "All required category fields must be provided in the request.",
            tags = {"Categories (Admin)"}
    )
    @PostMapping()
    public ResponseEntity<SimpleResponse> addCategory(@Valid @RequestBody CategoryAdminRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_CREATED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Delete category",
            description = "Deletes an existing category identified by its ID. " +
                    "This operation permanently removes the category from the system.",
            tags = {"Categories (Admin)"}
    )
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<SimpleResponse> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_DELETED),
                HttpStatus.OK
        );
    }

}

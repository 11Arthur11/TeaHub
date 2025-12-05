package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.shop.admin.CategoryAdminRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {


    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return null; //TODO
    }


    @PostMapping()
    public ResponseEntity<SimpleResponse> addCategory(@Valid @RequestBody CategoryAdminRequest category) {
        return null; //TODO
    }

}

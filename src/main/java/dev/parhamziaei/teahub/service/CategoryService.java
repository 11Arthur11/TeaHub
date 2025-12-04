package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.shop.admin.CategoryAdminRequest;
import dev.parhamziaei.teahub.dto.response.shop.admin.CategoryDetailAdminResponse;

import dev.parhamziaei.teahub.dto.response.shop.user.CategoryListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryListResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryListResponse.class))
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDetailAdminResponse getCategoryDetailById(Long id) {
        return modelMapper.map(categoryRepository.findById(id), CategoryDetailAdminResponse.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void addCategory(CategoryAdminRequest request) {
        Category category = modelMapper.map(request, Category.class);
        categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(CategoryAdminRequest request) {
        Category category = modelMapper.map(request, Category.class);
        categoryRepository.update(category);
    }

}

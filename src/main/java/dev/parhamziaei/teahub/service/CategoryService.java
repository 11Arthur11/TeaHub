package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.shop.admin.CategoryAdminRequest;

import dev.parhamziaei.teahub.dto.response.shop.admin.CategoryListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.CategoryListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.ConflictEntityException;
import dev.parhamziaei.teahub.exception.custom.global.EntityInUseException;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;

    public List<CategoryListResponse> getAvailableCategories() {
       return categoryRepo.findAll()
                .stream()
                .filter(Category::isActive)
                .map(c -> modelMapper.map(c, CategoryListResponse.class))
                .toList();
    }

    public List<CategoryListAdminResponse> getAllCategories() {
        List<CategoryListAdminResponse> responses = categoryRepo.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryListAdminResponse.class))
                .toList();
        if (responses.isEmpty())
            throw new NoSuchDataException("No Category Initiated");
        return responses;
    }

    public void addCategory(CategoryAdminRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(request.getSlug())
                .active(request.isActive())
                .build();
        
        if (categoryRepo.existsByCategory(category))
            throw new ConflictEntityException("Category Already Defined");
        else
            categoryRepo.save(category);

    }

    public void updateCategory(Long categoryId, CategoryAdminRequest request) {
        Category category = modelMapper.map(request, Category.class);
        category.setId(categoryId);
        if (categoryRepo.existsByCategory(category))
            throw new ConflictEntityException("Category Exists");
        else
            categoryRepo.update(category);
    }

    public void deleteCategory(Long categoryId) {
        if (categoryRepo.hasProduct(categoryId))
            throw new EntityInUseException("Category has product children");
        else
            categoryRepo.delete(categoryId);
    }

}

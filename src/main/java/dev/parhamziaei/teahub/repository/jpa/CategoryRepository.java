package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);
    Optional<Category> findBySlug(String slug);
    List<Category> findAll();
    boolean existsByCategory(Category category);
    boolean hasProduct(Long categoryId);
    void save(Category category);
    void update(Category category);
    void delete(Long categoryId);

}

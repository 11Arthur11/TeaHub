package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.enums.ProductsType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    Optional<Category> findBySlug(String slug);
    List<Category> findAll();
    List<Category> findAllByEnabled(boolean enabled);
    List<Category> findAllByType(ProductsType type);
    boolean existsByCategory(Category category);
    void save(Category category);
    void update(Category category);
    void delete(Category category);

}

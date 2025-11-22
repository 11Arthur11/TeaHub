package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.enums.ProductsType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final EntityManager em;

    @Override
    public Optional<Category> findById(Long id) {
        return em.createQuery("select c from Category c where c.id = :id", Category.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Category> findByName(String name) {
        return em.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return em.createQuery("SELECT c FROM Category c WHERE c.slug = :slug", Category.class)
                .setParameter("slug", slug)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c", Category.class)
                .getResultList();
    }

    @Override
    public List<Category> findAllByEnabled(boolean enabled) {
        return em.createQuery("SELECT c FROM Category c WHERE c.active = :active", Category.class)
                .setParameter("active", enabled)
                .getResultList();
    }

    @Override
    public List<Category> findAllByType(ProductsType type) {
        return em.createQuery("SELECT c FROM Category c WHERE c.productsType = :type", Category.class)
                .setParameter("type", type)
                .getResultList();
    }

    @Override
    public boolean existsByCategory(Category category) {
        return em.createQuery("SELECT COUNT(c) FROM Category c WHERE c.slug = :slug AND c.name = :name", Long.class)
                .setParameter("slug", category.getSlug())
                .setParameter("name", category.getName()).getSingleResult() > 0;
    }

    @Override
    public void save(Category category) {
        em.persist(category);
    }

    @Override
    public void update(Category category) {
        em.merge(category);
    }

    @Override
    public void delete(Category category) {
        em.remove(category);
    }

}

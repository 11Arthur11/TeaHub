package dev.parhamziaei.teahub.repository.jpa.implement;

import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
    public boolean existsByCategory(Category category) {
        return em.createQuery("SELECT COUNT(c) FROM Category c WHERE c.slug = :slug AND c.name = :name", Long.class)
                .setParameter("slug", category.getSlug())
                .setParameter("name", category.getName()).getSingleResult() > 0;
    }

    @Override
    public boolean hasProduct(Long categoryId) {
        Category category = em.createQuery("SELECT c FROM Category c WHERE c.id = :id", Category.class)
                .setParameter("id", categoryId)
                .getResultList()
                .stream()
                .findFirst()
                .orElseThrow(NoSuchEntityException::new);
        return !category.getProducts().isEmpty();
    }

    @Transactional
    @Override
    public void save(Category category) {
        em.persist(category);
    }

    @Transactional
    @Override
    public void update(Category category) {
        em.merge(category);
    }

    @Transactional
    @Override
    public void delete(Long categoryId) {
        em.remove(em.find(Category.class, categoryId));
    }

}

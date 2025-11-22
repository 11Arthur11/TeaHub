package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeaSpeakProductRepositoryImpl implements TeaSpeakProductRepository {

    private final EntityManager em;

    @Override
    public Optional<TeaSpeakProduct> findById(Long id) {
        return em.createQuery("select p from TeaSpeakProduct p where p.id = :id", TeaSpeakProduct.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public List<TeaSpeakProduct> findAll() {
        return em.createQuery("SELECT p FROM TeaSpeakProduct p",  TeaSpeakProduct.class)
                .getResultList();
    }

    @Override
    public void save(TeaSpeakProduct teaSpeakProduct) {
        em.persist(teaSpeakProduct);
    }

    @Override
    public void update(TeaSpeakProduct teaSpeakProduct) {
        em.merge(teaSpeakProduct);
    }

    @Override
    public void delete(TeaSpeakProduct teaSpeakProduct) {
        em.remove(teaSpeakProduct);
    }
}

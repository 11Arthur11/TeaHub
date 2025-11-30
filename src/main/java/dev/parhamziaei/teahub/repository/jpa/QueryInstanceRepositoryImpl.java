package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.QueryInstanceStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QueryInstanceRepositoryImpl implements QueryInstanceRepository {

    private final EntityManager em;

    @Override
    public Optional<QueryInstance> findById(Long id) {
        return em.createQuery("SELECT y FROM QueryInstance y WHERE y.id =:id", QueryInstance.class).setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<QueryInstance> findByAddress(String ip, Integer port) {
        return em.createQuery("SELECT y FROM QueryInstance y WHERE y.credentials.ip =:ip AND y.credentials.port = :port", QueryInstance.class)
                .setParameter("ip", ip)
                .setParameter("port", port)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public List<QueryInstance> findAll() {
        return em.createQuery("SELECT y FROM QueryInstance y", QueryInstance.class).getResultList();
    }

    @Override
    public List<QueryInstance> findByStatus(QueryInstanceStatus status) {
        return em.createQuery("SELECT y FROM QueryInstance y WHERE y.status = :status", QueryInstance.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Transactional
    @Override
    public void save(QueryInstance queryInstance) {
        em.persist(queryInstance);
    }

    @Transactional
    @Override
    public void delete(QueryInstance queryInstance) {
        em.remove(queryInstance);
    }

    @Transactional
    @Override
    public void update(QueryInstance queryInstance) {
        em.merge(queryInstance);
    }

    @Override
    public boolean existByAddress(String ip, Integer port) {
        return em.createQuery("select COUNT(y) from QueryInstance y where y.credentials.ip = :ip AND y.credentials.port = :port", Long.class)
                .setParameter("ip", ip)
                .setParameter("port", port)
                .getSingleResult() > 0;
    }
}

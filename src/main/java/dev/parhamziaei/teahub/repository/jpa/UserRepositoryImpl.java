package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;

    @Override
    @Transactional
    public void save(User user) {
        em.persist(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        em.merge(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        em.remove(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return em.createQuery("SELECT u FROM User u where u.id =:id", User.class).setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return em.createQuery("select u from User u where u.phone = :phone", User.class)
                .setParameter("phone", phoneNumber)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return em.createQuery("select COUNT(u) from User u where u.phone = :phone", Long.class)
                .setParameter("phone", phoneNumber)
                .getSingleResult() > 0;
    }

}

package dev.parhamziaei.teahub.repository.jpa.implement;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.repository.jpa.UserCustomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager em;

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

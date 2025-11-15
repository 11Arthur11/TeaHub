package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.RefreshToken;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final EntityManager em;

    @Transactional
    public void save(RefreshToken refreshToken) {
        em.persist(refreshToken);
    }

    @Transactional
    public void update(RefreshToken refreshToken) {
        em.merge(refreshToken);
    }

    @Transactional
    public void delete(RefreshToken refreshToken) {
        em.remove(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return em.createQuery("SELECT r FROM RefreshToken r WHERE r.token = :token", RefreshToken.class)
                .setParameter("token", token)
                .getResultList()
                .stream()
                .findFirst();
    }

}

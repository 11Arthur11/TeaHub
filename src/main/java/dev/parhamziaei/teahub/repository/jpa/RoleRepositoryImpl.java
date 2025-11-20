package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.user.Role;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final EntityManager em;

    @Transactional
    public void save(Role role) {
        em.persist(role);
    }

    public List<Role> findAll() {
        return em.createQuery("select r from Role r", Role.class).getResultList();
    }

    public Optional<Role> findByName(String roleName) {
        return em.createQuery("SELECT r FROM Role r WHERE r.name = :roleName", Role.class).setParameter("roleName", roleName).getResultList()
                .stream()
                .findFirst();
    }

}

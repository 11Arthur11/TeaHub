package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    void save(Role role);

    List<Role> findAll();

    Optional<Role> findByName(String roleName);

}

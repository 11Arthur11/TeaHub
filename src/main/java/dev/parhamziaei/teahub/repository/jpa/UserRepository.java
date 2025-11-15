package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(User user);
    Optional<User> findById(Long id);
    Optional<User> findByPhoneNumber(String name);
    boolean existsByPhoneNumber(String phoneNumber);


}

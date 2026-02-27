package dev.parhamziaei.teahub.test_util;

import dev.parhamziaei.teahub.entity.jpa.user.Role;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.UserSetting;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.user.Roles;
import dev.parhamziaei.teahub.exception.custom.authorization.NoSuchRoleException;
import dev.parhamziaei.teahub.repository.jpa.RoleRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class UserTestUtil {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static final List<String> DUMMIES_PHONES = new ArrayList<>();

    @PostConstruct
    public void initRoles() {
        List<String> dbRoles = roleRepository.findAll().stream().map(Role::getName).toList();
        List<Roles> internalRoles = Arrays.stream(Roles.values()).toList();
        internalRoles.forEach(role -> {
            if (!dbRoles.contains(role.value())) {
                roleRepository.save(new Role(role.value(), role.hierarchy()));
            }
        });
    }

    @Transactional
    public User persistedDummyAdminUser() {
        Random random = new Random();
        Role adminRole = roleRepository.findByName(Roles.ADMIN.value())
                .orElseThrow(() -> new NoSuchRoleException(Roles.ADMIN.value()));
        UserSetting userSetting = new UserSetting();
        User adminUser = User.builder()
                .phone("+989" + String.format("%08d", random.nextInt(100000000)))
                .firstName("persisted")
                .lastName("dummy-admin")
                .emailVerified(true)
                .build();

        adminUser.setRole(adminRole);
        adminUser.setWallet(new Wallet());
        adminUser.setSetting(userSetting);
        userRepository.save(adminUser);
        DUMMIES_PHONES.add(adminUser.getPhone());
        return adminUser;
    }

    @Transactional
    public User persistedDummyUser() {
        Random random = new Random();
        Role role = roleRepository.findByName(Roles.USER.value())
                .orElseThrow(() -> new NoSuchRoleException(Roles.USER.value()));
        UserSetting userSetting = new UserSetting();
        User user = User.builder()
                .email("persisted.dummy@gmail.top")
                .phone("+989" + String.format("%08d", random.nextInt(100000000)))
                .firstName("persisted")
                .lastName("dummy-user")
                .emailVerified(true)
                .build();

        user.setRole(role);
        user.setWallet(new Wallet());
        user.setSetting(userSetting);
        userRepository.save(user);
        DUMMIES_PHONES.add(user.getPhone());
        return user;
    }

    @EventListener(ContextClosedEvent.class)
    public void deletePersistedDummyUser() {
        userRepository.deleteAll(
                userRepository.findAll()
                .stream()
                .filter(u -> DUMMIES_PHONES.contains(u.getPhone()))
                .toList()
        );
    }

    public static User dummyUser() {
        Role role = new Role(Roles.USER.name(), Roles.USER.hierarchy());
        User user = User.builder()
                .phone("+989113783112")
                .email("john.doe@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();

        user.setWallet(new Wallet());
        return user;
    }

}

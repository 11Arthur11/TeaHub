package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.configuration.properties.InitializeProperties;
import dev.parhamziaei.teahub.entity.jpa.Role;
import dev.parhamziaei.teahub.entity.jpa.User;
import dev.parhamziaei.teahub.entity.jpa.UserSetting;
import dev.parhamziaei.teahub.entity.jpa.Wallet;
import dev.parhamziaei.teahub.enums.Roles;
import dev.parhamziaei.teahub.exception.custom.authorization.NoSuchRoleException;
import dev.parhamziaei.teahub.integration.ippanel.IPPanelService;
import dev.parhamziaei.teahub.repository.jpa.RoleRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.utils.PhoneNumbers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initialization implements CommandLineRunner {

    private final InitializeProperties initProperties;
    private final IPPanelService ipPanelService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
//        ipPanelService.sendTwoFactorSMS("0000", PhoneNumbers.formatedOf(initProperties.adminPhoneNumber()));
        initRoles();
        if (!userRepository.existsByPhoneNumber(initProperties.adminPhoneNumber()))
            initAdmin();
    }

    public void initRoles() {
        List<String> dbRoles = roleRepository.findAll().stream().map(Role::getName).toList();
        List<Roles> internalRoles = Arrays.stream(Roles.values()).toList();
        internalRoles.forEach(role -> {
            if (!dbRoles.contains(role.value())) {
                roleRepository.save(new Role(role.value(), role.hierarchy()));
                log.info("Role ({}) created by system", role.value());
            }
        });
    }

    private void initAdmin() {
        Role adminRole = roleRepository.findByName(Roles.ADMIN.value())
                .orElseThrow(() -> new NoSuchRoleException(Roles.ADMIN.value()));
        UserSetting userSetting = new UserSetting();
        Wallet wallet = new Wallet();
        User adminUser = User.builder()
                .email(initProperties.adminEmail())
                .phone(initProperties.adminPhoneNumber())
                .firstName(initProperties.adminFirstName())
                .lastName(initProperties.adminLastName())
                .roles(List.of(adminRole))
                .emailVerified(true)
                .build();
        adminUser.setWallet(wallet);
        adminUser.setSetting(userSetting);
        userRepository.save(adminUser);
    }

}

package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.configuration.properties.InitializeProperties;
import dev.parhamziaei.teahub.entity.jpa.ApplicationSetting;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.entity.jpa.user.Role;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.UserSetting;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.InstanceStatus;
import dev.parhamziaei.teahub.enums.Roles;
import dev.parhamziaei.teahub.exception.custom.authorization.NoSuchRoleException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.TelnetConnectionPool;
import dev.parhamziaei.teahub.repository.jpa.*;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initialization implements CommandLineRunner {

    private final InitializeProperties initProperties;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepo;
    private final TelnetConnectionPool telnetConnectionManager;
    private final ApplicationSettingRepository applicationSettingRepository;
    private final TeaSpeakProductRepository teaSpeakProductRepository;
    private final TeaSpeakResourceRepository teaSpeakResourceRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        ipPanelService.sendTwoFactorSMS("0000", PhoneNumbers.formatedOf(initProperties.adminPhoneNumber()));
        initApplicationSetting();
        log.info("Initialization-Operation -> Application settings initialized");
        initRoles();
        if (!userRepository.existsByPhoneNumber(initProperties.adminPhoneNumber()))
            initAdmin();
        initTestResource();
    }

    @Transactional
    public void initTestResource() {
//        User user = userRepository.findByPhoneNumber(initProperties.adminPhoneNumber())
//                .orElseThrow(NoSuchEntityException::new);
//
//        Category category = Category.builder()
//                .name("test")
//                .active(true)
//                .description("TEST DESCRIPTION")
//                .slug("test")
//                .build();
//
//        categoryRepo.save(category);
//
//        TeaSpeakProduct product = TeaSpeakProduct.builder()
//                .price(new Money(BigDecimal.valueOf(55000)))
//                .productName("test product")
//                .maxClients(32)
//                .expiration(Duration.ofDays(7))
//                .build();
//
//        category.appendProduct(product);
//        teaSpeakProductRepository.save(product);
//
//        TeaSpeakResource teaSpeakResource = TeaSpeakResource.builder()
//                .label("test")
//                .owner(user)
//                .product(product)
//                .maxClients(product.getMaxClients())
//                .status(InstanceStatus.DEPLOYING)
//
//                .expiration(LocalDateTime.now().plus(product.getExpiration()))
//                .build();
//
//        teaSpeakResourceRepository.save(teaSpeakResource);
    }

    public void initApplicationSetting() {
        if (applicationSettingRepository.findAll().isEmpty())
            applicationSettingRepository.save(new ApplicationSetting());
    }

    public void initRoles() {
        List<String> dbRoles = roleRepository.findAll().stream().map(Role::getName).toList();
        List<Roles> internalRoles = Arrays.stream(Roles.values()).toList();
        internalRoles.forEach(role -> {
            if (!dbRoles.contains(role.value())) {
                roleRepository.save(new Role(role.value(), role.hierarchy()));
                log.info("Initialization-Operation -> Role ({}) created by system", role.value());
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

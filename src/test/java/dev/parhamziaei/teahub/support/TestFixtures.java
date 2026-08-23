package dev.parhamziaei.teahub.support;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.Role;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.enums.user.Roles;
import dev.parhamziaei.teahub.valueobject.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TestFixtures {

    private TestFixtures() {
    }

    public static User user(long userId, long walletId, BigDecimal balance) {
        User user = User.builder()
                .phone("9000000000")
                .email("user@test.invalid")
                .firstName("Test")
                .lastName("User")
                .build();
        user.setId(userId);
        user.setRole(new Role(Roles.ROLE_USER.value(), Roles.ROLE_USER.hierarchy()));
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(new Money(balance));
        user.setWallet(wallet);
        return user;
    }

    public static User admin(long userId, long walletId, BigDecimal balance) {
        User user = user(userId, walletId, balance);
        user.setRole(new Role(Roles.ROLE_ADMIN.value(), Roles.ROLE_ADMIN.hierarchy()));
        return user;
    }

    public static BillableProduct product(long productId, BigDecimal price, ProductPeriod period) {
        BillableProduct product = BillableProduct.builder()
                .productName("Test product")
                .price(new Money(price))
                .period(period)
                .expiration(period.duration())
                .enabled(true)
                .build();
        product.setId(productId);
        return product;
    }

    public static BillableResource resource(
            long resourceId,
            User owner,
            BillableProduct product,
            ResourceStatus status,
            LocalDateTime expiration
    ) {
        BillableResource resource = BillableResource.builder()
                .owner(owner)
                .product(product)
                .resourceStatus(status)
                .resourceType(ResourceType.TEASPEAK)
                .expiration(expiration)
                .orderDate(LocalDateTime.now().minusDays(1))
                .autoProlong(true)
                .label("Test resource")
                .build();
        resource.setId(resourceId);
        return resource;
    }
}

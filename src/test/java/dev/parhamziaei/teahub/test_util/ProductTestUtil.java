package dev.parhamziaei.teahub.test_util;

import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.BillableProductRepository;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.ProductRepository;
import dev.parhamziaei.teahub.valueobject.Money;
import dev.parhamziaei.teahub.valueobject.ProductPresentation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductTestUtil {

    @Autowired
    private CategoryTestUtil categoryTestUtil;
    @Autowired
    private BillableProductRepository billableProductRepository;

    @Transactional
    public BillableProduct persistedDummyProduct(String name, Duration resourceExpiry, ProductPeriod period, BigDecimal price, boolean enabled) {
        Category category = categoryTestUtil.persistedDummyCategory(true);

        BillableProduct product = BillableProduct.builder()
                .productName(name)
                .price(new Money(price))
                .enabled(enabled)
                .presentation(new ProductPresentation("AAA", "\"a\": 2", "\"b\": 3"))
                .expiration(resourceExpiry)
                .period(period)
                .build();

        category.addProduct(product);
        billableProductRepository.save(product);
        return product;
    }

    @Transactional
    public BillableProduct randomizedPersistedDummyProduct(BigDecimal price) {
        Category category = categoryTestUtil.persistedDummyCategory(true);

        BillableProduct product = BillableProduct.builder()
                .productName("dummy product - " + UUID.randomUUID())
                .price(new Money(price))
                .enabled(true)
                .expiration(Duration.ofDays(30))
                .period(ProductPeriod.MONTHLY)
                .build();

        category.addProduct(product);
        billableProductRepository.save(product);
        return product;
    }

}

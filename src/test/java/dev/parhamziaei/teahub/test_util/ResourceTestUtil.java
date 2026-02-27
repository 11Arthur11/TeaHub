package dev.parhamziaei.teahub.test_util;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ResourceTestUtil {

    @Autowired
    private UserTestUtil userTestUtil;

    private final static List<Long> DUMMIES = new ArrayList<>();
    @Autowired
    private BillableResourceRepository billableResourceRepository;
    @Autowired
    private ProductTestUtil productTestUtil;

    @Transactional
    public BillableResource persistedDummyBillableResource(
            ResourceStatus resourceStatus,
            LocalDateTime orderDate,
            LocalDateTime expiration,
            boolean autoProlong,
            BigDecimal price
    ) {
        User dummyUser = userTestUtil.persistedDummyUser();
        BillableProduct billableProduct = productTestUtil.randomizedPersistedDummyProduct(price);

        BillableResource billableResource = BillableResource.builder()
                .resourceStatus(resourceStatus)
                .orderDate(orderDate)
                .label("dummy resource - " + UUID.randomUUID() )
                .autoProlong(autoProlong)
                .expiration(expiration)
                .resourceType(ResourceType.TEASPEAK)
                .build();

        billableProduct.addUserResource(billableResource);
        billableResource.setOwner(dummyUser);
        billableResourceRepository.save(billableResource);
        DUMMIES.add(billableResource.getId());
        return billableResource;
    }

    @EventListener(ContextClosedEvent.class)
    public void deletePersistedDummyBillableResource() {
        billableResourceRepository.deleteAll(
                billableResourceRepository.findAllById(DUMMIES)
        );
    }

}

package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.test_util.ResourceTestUtil;
import dev.parhamziaei.teahub.test_util.UserTestUtil;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest
class ResourceServiceTest {

    @Autowired
    private UserTestUtil userTestUtil;
    @Autowired
    private ResourceTestUtil resourceTestUtils;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private BillableResourceRepository billableResourceRepository;

    @Test
    @Transactional
    void findResource_InvalidUser_ShouldThrowException() {
        User anotherUser = userTestUtil.persistedDummyUser();
        BillableResource resource = resourceTestUtils.persistedDummyBillableResource(
                ResourceStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now().plus(Duration.ofMinutes(5)),
                true,
                new BigDecimal(2)
        );

        Assertions.assertThrows(
                NoSuchEntityException.class,
                () -> resourceService.findResourceByUser(anotherUser.getId(), resource.getId())
        );
    }

//    @Test
//    @Transactional
//    void findResource_ShouldFindResource() {
//        BillableResource resource = resourceTestUtils.persistedDummyBillableResource(
//                ResourceStatus.ACTIVE,
//                LocalDateTime.now(),
//                LocalDateTime.now().plus(Duration.ofMinutes(5)),
//                true,
//                new BigDecimal(2)
//        );
//
//        AbstractResourceDetailResponse result = Assertions.assertDoesNotThrow( () ->
//                resourceService.findResourceByUser(resource.getOwner().getId(), resource.getId())
//        );
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(result.getId(), resource.getId());
//        Assertions.assertEquals(result.getResourceType(), resource.getResourceType());
//    }

    @Test
    @Transactional
    void prolongResource() {
        BillableResource resource = resourceTestUtils.persistedDummyBillableResource(
                ResourceStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now().plus(Duration.ofMinutes(5)),
                true,
                new BigDecimal(2)
        );

        Wallet ownerWallet = resource.getOwner().getWallet();
        ownerWallet.setBalance(new Money(new BigDecimal(2)));
        final LocalDateTime expirationBeforeProlong = resource.getExpiration();
        resourceService.prolongResource(resource.getOwner().getId(), resource.getId());
        Assertions.assertNotEquals(new BigDecimal(2), ownerWallet.getBalance().getAmount());
        Assertions.assertTrue(
                billableResourceRepository.findById(resource.getId())
                        .orElseThrow(NoSuchEntityException::new)
                        .getExpiration().isAfter(expirationBeforeProlong)
        );
    }
}
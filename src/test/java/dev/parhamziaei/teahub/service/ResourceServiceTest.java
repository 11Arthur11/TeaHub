package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.kafka.producer.ResourceEventProducer;
import dev.parhamziaei.teahub.repository.jpa.BillableProductRepository;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.service.deployment.DeploymentStrategyFactory;
import dev.parhamziaei.teahub.service.deployment.strategy.DeploymentStrategyHandler;
import dev.parhamziaei.teahub.service.mapper.BillableResourceMapStruct;
import dev.parhamziaei.teahub.service.mapper.resource.ResourceMapperFactory;
import dev.parhamziaei.teahub.support.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock private DeploymentStrategyFactory deploymentFactory;
    @Mock private DeploymentStrategyHandler deploymentHandler;
    @Mock private WalletService walletService;
    @Mock private BillableProductRepository productRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private BillableResourceRepository resourceRepository;
    @Mock private ResourceMapperFactory mapperFactory;
    @Mock private BillableResourceMapStruct resourceMapper;
    @Mock private ResourceEventProducer resourceEventProducer;

    private ResourceService resourceService;

    @BeforeEach
    void setUp() {
        resourceService = new ResourceService(
                deploymentFactory,
                walletService,
                productRepository,
                modelMapper,
                resourceRepository,
                mapperFactory,
                resourceMapper,
                resourceEventProducer
        );
    }

    @Test
    void newResourceChecksProductAndBalanceBeforeProvisioning() {
        AbstractNewResourceRequest request = mock(AbstractNewResourceRequest.class);
        BillableProduct product = TestFixtures.product(5L, new BigDecimal("120"), ProductPeriod.MONTHLY);
        when(request.getProductId()).thenReturn(5L);
        when(request.getType()).thenReturn(ResourceType.TEASPEAK);
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));
        when(deploymentFactory.getStrategy(ResourceType.TEASPEAK)).thenReturn(deploymentHandler);

        resourceService.newBillableResource(11L, request);

        verify(walletService).assertSufficientBalance(11L, new BigDecimal("120"));
        verify(deploymentHandler).initializeDeploy(request, 11L);
    }

    @Test
    void disabledProductCannotBeProvisioned() {
        AbstractNewResourceRequest request = mock(AbstractNewResourceRequest.class);
        BillableProduct product = TestFixtures.product(5L, new BigDecimal("120"), ProductPeriod.MONTHLY);
        product.setEnabled(false);
        when(request.getProductId()).thenReturn(5L);
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));

        assertThrows(NoSuchEntityException.class, () -> resourceService.newBillableResource(11L, request));
        verifyNoInteractions(walletService, deploymentFactory);
    }

    @Test
    void nonRenewableExpiredResourceMovesToPendingState() {
        BillableResource resource = resource(ResourceStatus.ACTIVE, LocalDateTime.now().minusMinutes(1));
        resource.setAutoProlong(false);
        when(resourceRepository.findById(100L)).thenReturn(Optional.of(resource));

        resourceService.resourceExpiredHandler(100L);

        assertEquals(ResourceStatus.PENDING_PROLONG, resource.getResourceStatus());
        verifyNoInteractions(walletService, deploymentFactory);
    }

    @Test
    void insufficientRenewalBalanceSuspendsExpiredResource() {
        BillableResource resource = resource(ResourceStatus.ACTIVE, LocalDateTime.now().minusMinutes(1));
        when(resourceRepository.findById(100L)).thenReturn(Optional.of(resource));
        when(resourceRepository.findOneByOwnerId(1L, 100L)).thenReturn(Optional.of(resource));
        when(deploymentFactory.getStrategy(ResourceType.TEASPEAK)).thenReturn(deploymentHandler);
        doThrow(InsufficientBalanceException.class).when(walletService)
                .debit(10L, new BigDecimal("50"), TransactionReason.PROLONG, 100L);

        resourceService.resourceExpiredHandler(100L);

        assertEquals(ResourceStatus.PENDING_PROLONG, resource.getResourceStatus());
        verify(deploymentHandler).suspend(resource);
        verify(resourceRepository).save(resource);
    }

    @Test
    void prolongDebitsOwnerExtendsExpiryAndResumesPendingResource() {
        LocalDateTime expiry = LocalDateTime.now().withNano(0);
        BillableResource resource = resource(ResourceStatus.PENDING_PROLONG, expiry);
        when(resourceRepository.findOneByOwnerId(1L, 100L)).thenReturn(Optional.of(resource));
        when(deploymentFactory.getStrategy(ResourceType.TEASPEAK)).thenReturn(deploymentHandler);

        resourceService.prolongResource(1L, 100L);

        verify(walletService).debit(10L, new BigDecimal("50"), TransactionReason.PROLONG, 100L);
        assertEquals(expiry.plusDays(30), resource.getExpiration());
        assertEquals(ResourceStatus.ACTIVE, resource.getResourceStatus());
        verify(deploymentHandler).resume(resource);
    }

    @Test
    void deployFailureRefundsOwnerAndSchedulesResourceDeletion() {
        BillableResource resource = resource(ResourceStatus.DEPLOYING, LocalDateTime.now().plusDays(30));
        when(resourceRepository.findById(100L)).thenReturn(Optional.of(resource));

        resourceService.handleDeployFailed(100L);

        verify(walletService).credit(1L, new BigDecimal("50"), TransactionReason.REFUND);
        assertNull(resource.getOwner());
        verify(resourceRepository).save(resource);
        verify(resourceEventProducer).sendResourceDeleteEvent(100L);
    }

    @Test
    void lockSuspendsResourceAndDeleteUsesDeploymentHandler() {
        BillableResource resource = resource(ResourceStatus.ACTIVE, LocalDateTime.now().plusDays(30));
        when(resourceRepository.findById(100L)).thenReturn(Optional.of(resource));
        when(deploymentFactory.getStrategy(ResourceType.TEASPEAK)).thenReturn(deploymentHandler);

        resourceService.lockResource(100L);

        assertEquals(ResourceStatus.LOCKED, resource.getResourceStatus());
        verify(deploymentHandler).suspend(resource);
        verify(resourceRepository).save(resource);

        resourceService.deleteResource(100L);
        verify(deploymentHandler).delete(resource);
        verify(resourceRepository).delete(resource);
    }

    @Test
    void unlockOfUnexpiredResourceResumesIt() {
        BillableResource resource = resource(ResourceStatus.LOCKED, LocalDateTime.now().plusDays(1));
        when(resourceRepository.findById(100L)).thenReturn(Optional.of(resource));
        when(deploymentFactory.getStrategy(ResourceType.TEASPEAK)).thenReturn(deploymentHandler);

        resourceService.unlockResource(100L);

        assertEquals(ResourceStatus.ACTIVE, resource.getResourceStatus());
        verify(deploymentHandler).resume(resource);
    }

    private BillableResource resource(ResourceStatus status, LocalDateTime expiry) {
        User owner = TestFixtures.user(1L, 10L, new BigDecimal("100"));
        BillableProduct product = TestFixtures.product(20L, new BigDecimal("50"), ProductPeriod.MONTHLY);
        return TestFixtures.resource(100L, owner, product, status, expiry.withNano(0));
    }
}

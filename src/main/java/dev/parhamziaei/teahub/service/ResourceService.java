package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.query.ResourceFilterRequest;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.BillableResourceEditRequest;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.ResourceMetric;
import dev.parhamziaei.teahub.dto.response.dashboard.user.ResourceOverviewResponse;
import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.ResourceListAdminResponse;
import dev.parhamziaei.teahub.dto.response.resource.ResourceListResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.kafka.producer.ResourceEventProducer;
import dev.parhamziaei.teahub.repository.jpa.*;
import dev.parhamziaei.teahub.repository.jpa.specification.BillableResourceSpecification;
import dev.parhamziaei.teahub.service.deployment.DeploymentStrategyFactory;
import dev.parhamziaei.teahub.service.mapper.BillableResourceMapStruct;
import dev.parhamziaei.teahub.service.mapper.resource.ResourceMapperFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final DeploymentStrategyFactory deploymentFactory;
    private final WalletService walletService;
    private final BillableProductRepository billableProductRepo;
    private final ModelMapper modelMapper;
    private final BillableResourceRepository billableResourceRepository;
    private final ResourceMapperFactory mapperFactory;
    private final BillableResourceMapStruct billableResourceMapStruct;
    private final ResourceEventProducer resourceEventProducer;

    @Transactional
    public void newBillableResource(Long userId, AbstractNewResourceRequest request) {
        BillableProduct product = billableProductRepo.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchEntityException("product not found"));

        if (!product.isEnabled())
            throw new NoSuchEntityException("product is not enabled");

        walletService.assertSufficientBalance(userId, product.getPrice().getAmount());

        deploymentFactory.getStrategy(request.getType())
                .initializeDeploy(request, userId);
    }

    public List<ResourceListResponse> getAllUserResources(Long userId) {
        List<ResourceListResponse> resourcesResponse = new ArrayList<>();
        Specification<BillableResource> tsSpec = BillableResourceSpecification.forOwnerId(userId);
        billableResourceRepository.findAll(tsSpec).forEach(resource -> {
            ResourceListResponse dto = modelMapper.map(resource, ResourceListResponse.class);
            dto.setProductName(resource.getProduct().getProductName());
            dto.setResourceType(resource.getResourceType());
            dto.setPeriod(resource.getProduct().getPeriod());
            resourcesResponse.add(dto);
        });
        return resourcesResponse;
    }

    public ResourceMetric getResourceMetric() {
        return billableResourceRepository.getResourceMetric();
    }

    @Transactional
    public PagedModel<ResourceListAdminResponse> getAllResources(ResourceFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Specification<BillableResource> spec = BillableResourceSpecification.byUserId(filter.getByOwnerId())
                .and(BillableResourceSpecification.byType(filter.getByType()))
                .and(BillableResourceSpecification.byStatus(filter.getByResourceStatus()));

        Page<ResourceListAdminResponse> resourcesPage = billableResourceRepository.findAll(spec, pageable)
                .map(r -> {
                    ResourceListAdminResponse dto = modelMapper.map(r, ResourceListAdminResponse.class);
                    dto.setProductName(r.getProduct().getProductName());
                    if (r.getOwner() == null)
                        return dto;
                    dto.setPeriod(r.getProduct().getPeriod());
                    switch (r.getResourceType()) {
                        case TEASPEAK -> {
                            TeaSpeakResource teaSpeakResource = (TeaSpeakResource) r;
                            if (teaSpeakResource.getParentQueryInstance() != null)
                                dto.setNodeId(teaSpeakResource.getParentQueryInstance().getId());
                        }
                        case AUDIO_BOT -> {
                            AudioBotResource audioBotResource = (AudioBotResource) r;
                            if (audioBotResource.getParentNode() != null)
                                dto.setNodeId(audioBotResource.getParentNode().getId());
                        }
                    }
                    return dto;
                });

        if (!resourcesPage.hasContent())
            throw new NoSuchDataException();

        return new PagedModel<>(resourcesPage);
    }

    @Transactional
    public AbstractResourceDetailResponse findResourceByUser(Long userId, Long resourceId) {
        BillableResource resource = billableResourceRepository.findOneByOwnerId(userId, resourceId)
                .orElseThrow(NoSuchEntityException::new);
        return mapperFactory.getHandler(resource.getResourceType()).mapResourceDetailResponse(resource);
    }

    @Transactional
    public AbstractResourceDetailResponse findResource(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);
        return mapperFactory.getHandler(resource.getResourceType()).mapResourceDetailAdminResponse(resource);
    }

    @Transactional
    public void resourceExpiredHandler(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        if (!resource.isAutoProlong()) {
            resource.setResourceStatus(ResourceStatus.PENDING_PROLONG);
            return;
        }

        try {
            prolongResource(resource.getOwner().getId(), resourceId);
        } catch (InsufficientBalanceException ignored) {
            resource.setResourceStatus(ResourceStatus.PENDING_PROLONG);

            deploymentFactory.getStrategy(resource.getResourceType())
                    .suspend(resource);

            // ! notify user via sms or email or something
        }

        billableResourceRepository.save(resource);
    }

    @Transactional
    public void lockResource(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);
        resource.setResourceStatus(ResourceStatus.LOCKED);
        billableResourceRepository.save(resource);
        deploymentFactory.getStrategy(resource.getResourceType())
                .suspend(resource);
    }

    @Transactional
    public void unlockResource(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        resource.setResourceStatus(
                ResourceStatus.ACTIVE
        );

        billableResourceRepository.save(resource);

        if (resource.isExpired()) {
            resourceExpiredHandler(resourceId);
            return;
        }

        deploymentFactory.getStrategy(resource.getResourceType())
                .resume(resource);
    }

    @Transactional
    public void prolongByInvoicePaid(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        BillableProduct product = resource.getProduct();

        // ? prolonging resource
        resource.setExpiration(
                resource.getExpiration().plus(product.getExpiration())
        );

        if (resource.getResourceStatus() == ResourceStatus.PENDING_PROLONG) {
            resource.setResourceStatus(ResourceStatus.ACTIVE);
            deploymentFactory.getStrategy(resource.getResourceType())
                    .resume(resource);
        }
    }

    @Transactional
    public void forceProlongResource(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        BillableProduct product = resource.getProduct();

        // ? prolonging resource
        resource.setExpiration(
                resource.getExpiration().plus(product.getExpiration())
        );

        if (resource.getResourceStatus() == ResourceStatus.PENDING_PROLONG) {
            resource.setResourceStatus(ResourceStatus.ACTIVE);
            deploymentFactory.getStrategy(resource.getResourceType())
                    .resume(resource);
        }
    }

    @Transactional
    public void prolongResource(Long userId, Long resourceId) {
        BillableResource resource = billableResourceRepository.findOneByOwnerId(userId, resourceId)
                .orElseThrow(NoSuchEntityException::new);

        User owner = resource.getOwner();

        BillableProduct product = resource.getProduct();

        walletService.debit(
                owner.getWallet().getId(),
                product.getPrice().getAmount(),
                TransactionReason.PROLONG,
                resourceId
        );

        // ? prolonging resource
        resource.setExpiration(
                resource.getExpiration().plus(product.getExpiration())
        );

        if (resource.getResourceStatus() == ResourceStatus.PENDING_PROLONG) {
            resource.setResourceStatus(ResourceStatus.ACTIVE);
            deploymentFactory.getStrategy(resource.getResourceType())
                    .resume(resource);
        }
    }

    public void editResource(Long userId, Long resourceId, BillableResourceEditRequest editRequest) {
        BillableResource resource = billableResourceRepository.findOneByOwnerId(userId, resourceId)
                .orElseThrow(NoSuchEntityException::new);

        billableResourceMapStruct.toEntity(editRequest, resource);
        billableResourceRepository.save(resource);
    }

    public ResourceOverviewResponse getResourceOverview(Long userId) {
        return billableResourceRepository.getOverview(userId);
    }

    @Transactional
    public void handleDeployFailed(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        walletService.credit(resource.getOwner().getId(), resource.getProduct().getPrice().getAmount(), TransactionReason.REFUND);
        resource.setOwner(null);
        billableResourceRepository.save(resource);
        resourceEventProducer.sendResourceDeleteEvent(resourceId);
    }

    @Transactional
    public void deleteResource(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        deploymentFactory.getStrategy(resource.getResourceType())
                .delete(resource);

        billableResourceRepository.delete(resource);

        // ! notify user via sms or email or something
    }

}

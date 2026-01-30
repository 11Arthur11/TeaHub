package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.query.ResourceFilterRequest;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.response.resource.BaseResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.admin.ResourceListAdminResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.user.ResourceListResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.*;
import dev.parhamziaei.teahub.repository.jpa.specification.BillableResourceSpecification;
import dev.parhamziaei.teahub.service.deployment.DeploymentStrategyFactory;
import dev.parhamziaei.teahub.service.mapper.resource.ResourceMapperFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final DeploymentStrategyFactory deploymentFactory;
    private final WalletService walletService;
    private final BillableProductRepository billableProductRepo;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final BillableResourceRepository billableResourceRepository;
    private final ResourceMapperFactory mapperFactory;

    @Transactional
    public void newBillableResource(Long userId, AbstractNewResourceRequest request) {
        BillableProduct product = billableProductRepo.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchEntityException("product not found"));

        if (!product.isEnabled())
            throw new NoSuchEntityException("product not enabled");

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
            dto.setResourceStatus(messageService.get(resource.getResourceStatus()));
            resourcesResponse.add(dto);
        });
        return resourcesResponse;
    }

    @Transactional
    public PagedModel<ResourceListAdminResponse> getAllResources(ResourceFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Specification<BillableResource> spec = BillableResourceSpecification.byUserId(filter.getByOwnerId())
                .and(BillableResourceSpecification.byType(filter.getByType()))
                .and(BillableResourceSpecification.byStatus(filter.getByResourceStatus()));

        Page<BillableResource> resourcesPage = billableResourceRepository.findAll(spec, pageable);
        List<ResourceListAdminResponse> mapped = resourcesPage.getContent()
                .stream()
                .map(r -> {
                    ResourceListAdminResponse dto = modelMapper.map(r, ResourceListAdminResponse.class);
                    dto.setProductName(r.getProduct().getProductName());
                    dto.setResourceStatus(messageService.get(r.getResourceStatus()));
                    dto.setOwnerPhone(r.getOwner().getPhone());
                    return dto;
                }).toList();
        Page<ResourceListAdminResponse> mappedPage = new PageImpl<>(mapped, pageable, resourcesPage.getTotalElements());

        return new PagedModel<>(mappedPage);
    }

    @Transactional
    public BaseResourceDetailResponse findResourceByUser(Long userId, Long resourceId) {
        BillableResource resource = billableResourceRepository.findOneByOwnerId(userId, resourceId)
                .orElseThrow(NoSuchEntityException::new);
        return mapperFactory.getHandler(resource.getResourceType()).mapResourceDetailResponse(resource);
    }

    @Transactional
    public BaseResourceDetailResponse findResource(Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);
        return mapperFactory.getHandler(resource.getResourceType()).mapResourceDetailAdminResponse(resource);
    }

    @Transactional
    public void resourceExpiredHandler (Long resourceId) {
        BillableResource resource = billableResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        BillableProduct product = resource.getProduct();

        User owner = resource.getOwner();

        try {
            walletService.debit(
                    owner.getWallet().getId(),
                    product.getPrice().getAmount(),
                    TransactionReason.PROLONG,
                    resourceId
            );

            resource.setExpiration(LocalDateTime.now().plus(product.getExpiration()));
        } catch (InsufficientBalanceException ignored) {
            deploymentFactory.getStrategy(resource.getResourceType())
                    .suspend(resource);

            resource.setResourceStatus(ResourceStatus.PENDING_PROLONG);

            // ! notify user via sms or email or something
        }
    }

    @Transactional
    public void prolongResource (Long userid, Long resourceId) {
        BillableResource resource = billableResourceRepository.findOneByOwnerId(userid, resourceId)
                .orElseThrow(NoSuchEntityException::new);

        User owner = resource.getOwner();

        BillableProduct product = resource.getProduct();

        walletService.debit(
                owner.getWallet().getId(),
                product.getPrice().getAmount(),
                TransactionReason.PROLONG,
                resourceId
        );

        if (resource.getResourceStatus() == ResourceStatus.PENDING_PROLONG) {
            resource.setResourceStatus(ResourceStatus.ACTIVE);
            deploymentFactory.getStrategy(resource.getResourceType())
                    .resume(resource);
        }
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

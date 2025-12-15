package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.query.ResourceFilterRequest;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.response.resource.BaseResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.admin.ResourceListAdminResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.user.ResourceListResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
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
        BillableProduct product = billableProductRepo.findById(request.getProductId()) // ! what happen if product was not active?
                .orElseThrow(NoSuchEntityException::new);

        walletService.assertSufficientBalance(userId, product.getPrice().getAmount());

        deploymentFactory.getStrategy(request.getType())
                .deploy(request, userId);
    }

    public List<ResourceListResponse> getAllUserResources(Long userId) {
        List<ResourceListResponse> resourcesResponse = new ArrayList<>();
        Specification<BillableResource> tsSpec = BillableResourceSpecification.forUserId(userId);
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
        Specification<BillableResource> spec = BillableResourceSpecification.byOwnerPhone(filter.getByOwnerPhone())
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



}

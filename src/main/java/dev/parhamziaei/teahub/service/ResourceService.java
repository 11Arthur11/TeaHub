package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.response.resource.BaseResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.ResourceListResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.*;
import dev.parhamziaei.teahub.repository.jpa.specification.TeaSpeakResourceSpecification;
import dev.parhamziaei.teahub.service.deployment.DeploymentStrategyFactory;
import dev.parhamziaei.teahub.service.mapper.resource.ResourceMapperFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final TeaSpeakResourceRepository teaSpeakResourceRepo;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final BillableResourceRepository billableResourceRepository;
    private final ResourceMapperFactory mapperFactory;



    @Transactional
    public void newBillableResource(Long userId, AbstractNewResourceRequest request) {

        BillableProduct product = billableProductRepo.findById(request.getProductId())
                .orElseThrow(NoSuchEntityException::new);

        walletService.debit(userId, product.getPrice().getAmount());

        deploymentFactory.getStrategy(request.getType())
                .produceDeployEvent(request, userId);
    }

    public List<ResourceListResponse> getAllUserResources(Long userId) {
        List<ResourceListResponse> resourcesResponse = new ArrayList<>();
        Specification<TeaSpeakResource> tsSpec = TeaSpeakResourceSpecification.forUserId(userId);
        teaSpeakResourceRepo.findAll(tsSpec).forEach(tsResource -> {
            ResourceListResponse dto = modelMapper.map(tsResource, ResourceListResponse.class);
            dto.setProductName(tsResource.getProduct().getProductName());
            dto.setStatus(messageService.get(tsResource.getStatus()));
            resourcesResponse.add(dto);
        });

        // ! and do so with another resources

        return resourcesResponse;
    }

    @Transactional
    public BaseResourceDetailResponse findResourceById(Long userId, Long resourceId) {
        BillableResource resource = billableResourceRepository.findOneByOwnerId(userId, resourceId)
                .orElseThrow(NoSuchEntityException::new);
        return mapperFactory.getHandler(resource.getResourceType()).map(resource);
    }



}

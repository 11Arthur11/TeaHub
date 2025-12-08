package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.entity.jpa.resource.BaseResource;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.BillableProductRepository;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.ProductRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.service.deployment.DeploymentStrategyFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final DeploymentStrategyFactory deploymentFactory;
    private final ProductRepository productRepo;
    private final WalletService walletService;
    private final UserRepository userRepo;
    private final BillableProductRepository billableProductRepo;
    private final BillableResourceRepository billableResourceRepository;

    @Transactional
    public void newBillableResource(Long userId, AbstractNewResourceRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(NoSuchEntityException::new);

        BillableProduct product = billableProductRepo.findById(request.getProductId())
                .orElseThrow(NoSuchEntityException::new);

        walletService.debit(userId, product.getPrice().getAmount());

        BillableResource resource = BillableResource.builder()
                .label(request.getLabel())
                .owner(user)
                .product(product)
                .orderDate(LocalDateTime.now())
                .expiration(LocalDateTime.now().plus(product.getExpiration()))
                .status(ResourceStatus.DEPLOYING)
                .build();

        billableResourceRepository.save(resource);

        deploymentFactory.getStrategy(request.getResourceType())
                .produceDeployEvent(request, resource.getId());
    }

}

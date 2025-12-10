package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
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

        BillableProduct product = billableProductRepo.findById(request.getProductId())
                .orElseThrow(NoSuchEntityException::new);

        walletService.debit(userId, product.getPrice().getAmount());

        deploymentFactory.getStrategy(request.getType())
                .produceDeployEvent(request, userId);
    }

}

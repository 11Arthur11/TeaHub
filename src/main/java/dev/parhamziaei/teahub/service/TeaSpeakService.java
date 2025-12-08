package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.QueryCLI;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryCommandExecutionException;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakResourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeaSpeakService {

    private final QueryInstanceProperties queryProperties;
    private final QueryCLI queryCLI;
    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final QueryInstanceService queryInstanceService;
    private final BillableResourceRepository billableResourceRepo;
    private final TeaSpeakResourceRepository teaSpeakResourceRepository;

    @Transactional
    public void deployTeaSpeakInstance(Long resourceId, Integer maxClients) {
        final QueryInstance queryInstance = queryInstanceService.getAvailableQueryInstance();

        final BillableResource resource = billableResourceRepo.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        Optional<TeaSpeakResource> lastInstance = queryInstance.getInstances()
                .stream()
                .max(Comparator.comparing(TeaSpeakResource::getPort));

        final Integer instancePort = lastInstance.map(teaSpeakResource -> teaSpeakResource.getPort() + queryProperties.portStep())
                .orElseGet(queryInstance::getStartPort);

        TSCreateQueryRequest createRequest = TSCreateQueryRequest.builder()
                .maxClients(String.valueOf(maxClients))
                .port(String.valueOf(instancePort))
                .serverName(
                        generateInstanceName(
                                resource.getOwner().getPhone(),
                                resource.getId(),
                                resource.getExpiration()
                        )
                ).build();

        try {
            TSCreateQueryResponse queryResponse = queryCLI.createServer(queryInstance.getCredentials(), createRequest);

            TeaSpeakResource teaSpeakResource = TeaSpeakResource.builder()
                    .port(instancePort)
                    .maxClients(maxClients)
                    .sid(queryResponse.getSid())
                    .privilegeToken(queryResponse.getToken())
                    .status(ResourceStatus.ONLINE)
                    .build();
            teaSpeakResource.setId(resourceId);
            teaSpeakResource.setParentQueryInstance(queryInstance);
            teaSpeakResourceRepository.save(teaSpeakResource);

        } catch (QueryCommandExecutionException e) {
            // ! handle ReDeployment phase here
        }
    }

    private String generateInstanceName(String userPhone, Long resourceId, LocalDateTime expiration) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
        return userPhone + " - RID:" + resourceId + " - Exp:" + expiration.format(formatter);
    }

}

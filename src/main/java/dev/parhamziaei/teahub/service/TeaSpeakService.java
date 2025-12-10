package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.TeaSpeakResourceToken;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.QueryCLI;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeAddResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryCommandExecutionException;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakResourceRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
    private final TeaSpeakResourceRepository teaSpeakResourceRepository;

    @Transactional // ? this method always will called by kafka event handler
    public void deployTeaSpeakInstance(Long resourceId, Integer maxClients) {
        final QueryInstance queryInstance = queryInstanceService.getAvailableQueryInstance();

        final TeaSpeakResource teaSpeakResource = teaSpeakResourceRepository.findById(resourceId)
                .orElseThrow(NoSuchEntityException::new);

        Optional<TeaSpeakResource> lastInstance = queryInstance.getInstances()
                .stream()
                .max(Comparator.comparing(TeaSpeakResource::getPort));

        final Integer instancePort = lastInstance.map(r -> r.getPort() + queryProperties.portStep())
                .orElseGet(queryInstance::getStartPort);

        // ? creating TSCreate command object with teaSpeak product details
        TSCreateQueryRequest createRequest = TSCreateQueryRequest.builder()
                .maxClients(String.valueOf(maxClients))
                .port(String.valueOf(instancePort))
                .serverName(
                        generateInstanceName(
                                teaSpeakResource.getOwner().getFullName(),
                                teaSpeakResource.getId()
                        )
                ).build();

        try {
            // ? executing commands to query
            TSCreateQueryResponse createServerResponse = queryCLI.createServer(queryInstance.getCredentials(), createRequest);
            TSPrivilegeAddResponse privilegeAddResponse = queryCLI.generateNewQueryPrivilegeToken(
                    queryInstance.getCredentials(),
                    createServerResponse.getSid(),
                    String.valueOf(queryInstance.getDefaultQueryServerGroupId())
            );

            // ? creating privilege token with specified server group id
            TeaSpeakResourceToken privilegeToken = new TeaSpeakResourceToken(
                    Long.parseLong(privilegeAddResponse.getToken_id()),
                    privilegeAddResponse.getToken()
            );

            // ? updating billable resource as it deployed
            teaSpeakResource.setPort(instancePort);
            teaSpeakResource.setMaxClients(maxClients);
            teaSpeakResource.setSid(createServerResponse.getSid());
            teaSpeakResource.setStatus(ResourceStatus.ONLINE);
            teaSpeakResource.setParentQueryInstance(queryInstance);
            teaSpeakResource.addPrivilegeToken(privilegeToken);

            queryInstance.addInstance(teaSpeakResource);

            teaSpeakResourceRepository.save(teaSpeakResource);

        } catch (QueryCommandExecutionException e) {
            // TODO handle failover reDeployment phase here
        }
    }

    private String generateInstanceName(String fullName, Long resourceId) {
//        DateTimeFormatter formatter =
//                DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
        return fullName.replace(" ", "\\s") + "\\s-\\sRID:" + resourceId;
    }

}

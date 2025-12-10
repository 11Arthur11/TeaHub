package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResourceToken;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.QueryCLI;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeAddResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryCommandExecutionException;
import dev.parhamziaei.teahub.repository.jpa.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

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
    private final TeaSpeakResourceTokenRepository teaSpeakResourceTokenRepo;

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
                                teaSpeakResource.getLabel(),
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
            teaSpeakResource.setPrivilegeToken(privilegeToken);

            Hibernate.initialize(queryInstance.getInstances());
            queryInstance.addInstance(teaSpeakResource);

            teaSpeakResourceRepository.save(teaSpeakResource);

        } catch (QueryCommandExecutionException e) {
            // TODO handle failover reDeployment phase here
        }
    }



//    @Scheduled(cron = "0 */5 * * * *")
//    public void syncDBTokensWithQuery() {
//        List<TeaSpeakResource> teaSpeaks = teaSpeakResourceRepository.findAll();
//        teaSpeaks.forEach(ts -> {
//            List<TSPrivilegeListResponse> tsTokens = queryCLI.getPrivilegeTokens(ts.getParentQueryInstance().getCredentials(), ts.getSid());
//            ts.getPrivilegeTokens().forEach(dbToken -> {
//                Optional<TSPrivilegeListResponse> matchToken = tsTokens.stream().filter(t -> t.getToken().equals(dbToken.getToken())).findFirst();
//                if (matchToken.isPresent() && (Integer.parseInt(matchToken.get().getToken_use_count()) > 0))
//                    teaSpeakResourceTokenRepo.delete(dbToken);
//                if (matchToken.isEmpty())
//                    teaSpeakResourceTokenRepo.delete(dbToken);
//            });
//        });
//    }



    private String generateInstanceName(String label, Long resourceId) {
        return label.replace(" ", "\\s") + "\\s-\\sResourceID:\\s" + String.format("%06d", resourceId);
    }

}

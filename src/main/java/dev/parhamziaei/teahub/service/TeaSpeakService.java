package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResourceToken;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.enums.TeaSpeakStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.ActionNotExecutableException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.QueryCLI;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeAddResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSServerInfoResponse;
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
    private final QueryInstanceService queryInstanceService;
    private final TeaSpeakResourceRepository teaSpeakResourceRepository;
    private final TeaSpeakResourceTokenRepository teaSpeakResourceTokenRepo;
    private final UserRepository userRepo;

    @Transactional
    public TeaSpeakResource loadResourceByPermission(Long userId, Long resourceId) {
        User user = userRepo.findById(userId)
                .orElseThrow(NoSuchEntityException::new);

        return teaSpeakResourceRepository.findByOneByPermission(user, resourceId)
                .orElseThrow(NoSuchEntityException::new);
    }

    @Transactional // ? this method always will called by kafka event handler
    public void deployInstance(Long resourceId, Integer maxClients) {
        final QueryInstance queryInstance = queryInstanceService.getAvailableQueryInstance();

        TeaSpeakResource teaSpeakResource = teaSpeakResourceRepository.findById(resourceId)
                .orElseThrow(() -> new NoSuchEntityException("NO TEASPEAK RESOURCE FOUND WITH ID: " + resourceId));

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
            TSPrivilegeAddResponse privilegeAddResponse = queryCLI.generatePrivilegeToken(
                    queryInstance.getCredentials(),
                    createServerResponse.getSid(),
                    String.valueOf(queryInstance.getDefaultQueryServerGroupId())
            );

            // ? adding new generate privilege token for resource
            TeaSpeakResourceToken token = teaSpeakResource.getPrivilegeToken();
            token.setQueryId(Long.parseLong(privilegeAddResponse.getToken_id()));
            token.setToken(privilegeAddResponse.getToken());


            // ? updating billable resource as it deployed
            teaSpeakResource.setPort(instancePort);
            teaSpeakResource.setMaxClients(maxClients);
            teaSpeakResource.setSid(createServerResponse.getSid());
            teaSpeakResource.setResourceStatus(ResourceStatus.ACTIVE);
            teaSpeakResource.setTeaSpeakStatus(TeaSpeakStatus.ONLINE);

            Hibernate.initialize(queryInstance.getInstances());
            queryInstance.addInstance(teaSpeakResource);

            teaSpeakResourceRepository.save(teaSpeakResource);

        } catch (QueryCommandExecutionException e) {
            // TODO handle failover reDeployment phase here
        }
    }

    @Transactional
    public void syncWithQuery(TeaSpeakResource teaSpeakResource) {
        QueryInstance queryInstance = teaSpeakResource.getParentQueryInstance();

        TSServerInfoResponse info = queryCLI.getServerInfo(queryInstance.getCredentials(), teaSpeakResource.getSid());
        teaSpeakResource.setTeaSpeakStatus(TeaSpeakStatus.fromValue(info.getVirtualserver_status()));
    }

    @Transactional
    public void startInstance(Long userId, Long resourceId) {
        TeaSpeakResource resource = loadResourceByPermission(userId, resourceId);

        syncWithQuery(resource);

        if (resource.getTeaSpeakStatus() == TeaSpeakStatus.OFFLINE) {
            QueryInstance queryInstance = resource.getParentQueryInstance();
            queryCLI.startServer(queryInstance.getCredentials(), resource.getSid());
        } else {
            throw new ActionNotExecutableException("already started");
        }
    }

    @Transactional
    public void stopInstance(Long userId, Long resourceId) {
        TeaSpeakResource resource = loadResourceByPermission(userId, resourceId);

        syncWithQuery(resource);

        if (resource.getTeaSpeakStatus() == TeaSpeakStatus.ONLINE) {
            QueryInstance queryInstance = resource.getParentQueryInstance();
            queryCLI.stopServer(queryInstance.getCredentials(), resource.getSid());
        } else {
            throw new ActionNotExecutableException("already stopped");
        }
    }

    @Transactional
    public void refreshPrivilegeToken(Long userId, Long resourceId) {
        TeaSpeakResource resource = loadResourceByPermission(userId, resourceId);
        QueryInstance queryInstance = resource.getParentQueryInstance();
        TeaSpeakResourceToken currentToken = resource.getPrivilegeToken();

        queryCLI.getPrivilegeTokens(queryInstance.getCredentials(), resource.getSid())
                .stream()
                .filter(t -> t.getToken().equals(currentToken.getToken()) && t.getToken().equals(String.valueOf(currentToken.getQueryId())))
                .findFirst()
                .ifPresent(t -> {
                    queryCLI.deletePrivilegeToken(
                            queryInstance.getCredentials(),
                            resource.getSid(),
                            currentToken.getToken()
                    );
                });

        TSPrivilegeAddResponse newToken = queryCLI.generatePrivilegeToken(
                queryInstance.getCredentials(),
                resource.getSid(),
                queryInstance.getDefaultQueryServerGroupId().toString()
        );

        currentToken.setToken(newToken.getToken());
        currentToken.setQueryId(Long.parseLong(newToken.getToken_id()));
    }




    private String generateInstanceName(String label, Long resourceId) {
        return label.replace(" ", "\\s") + "\\s-\\sResourceID:\\s" + String.format("%06d", resourceId);
    }

}

package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.dto.response.teaspeak.QueryInstanceListResponse;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.InstancePortRangeNotValidException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.QueryInstanceAlreadyInitiatedException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.QueryInstanceNotFoundException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.TelnetConnectionPool;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.QueryInstanceStatus;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryConnectionPoolingException;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryLoginFailedException;
import dev.parhamziaei.teahub.integration.teaspeak_query.internal_service.ProvisionStrategyFactory;
import dev.parhamziaei.teahub.integration.teaspeak_query.internal_service.TeaSpeakProvisionStrategyHandler;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QueryInstanceService {

    private final QueryInstanceRepository queryInstanceRepo;
    private final TelnetConnectionPool connectionPool;
    private final QueryInstanceProperties queryInstanceProperties;
    private final TeaSpeakProvisionStrategyHandler strategyHandler;
    private final ModelMapper modelMapper;

    public QueryInstanceService(
            QueryInstanceRepository queryInstanceRepo,
            TelnetConnectionPool connectionPool,
            QueryInstanceProperties queryInstanceProperties,
            ProvisionStrategyFactory provisionStrategyFactory,
            ModelMapper modelMapper
    ) {
        this.queryInstanceRepo = queryInstanceRepo;
        this.connectionPool = connectionPool;
        this.queryInstanceProperties = queryInstanceProperties;
        this.strategyHandler = provisionStrategyFactory.getStrategy();
        this.modelMapper = modelMapper;
    }

    private boolean isPortRangeMatchSlots(QueryInstanceInitRequest request) {
        final int givenPortCount = request.getStopPort() - request.getStartPort();
        final int portStep = queryInstanceProperties.portStep();
        if (!(givenPortCount % portStep == 0)) {
            throw new InstancePortRangeNotValidException("port range dos not match the port step.");
        }
        return (givenPortCount / portStep) == request.getMaxTeaSpeakInstance();
    }

    public void changeQueryInstanceStatus(Long id, QueryInstanceStatus status) {
        QueryInstance queryInstance = queryInstanceRepo.findById(id)
                .orElseThrow(QueryInstanceNotFoundException::new);
        queryInstance.setStatus(status);
    }

    public void changeQueryInstanceStatus(String ip, Integer port, QueryInstanceStatus status) {
        QueryInstance queryInstance = queryInstanceRepo.findByAddress(ip, port)
                .orElseThrow(QueryInstanceNotFoundException::new);
        queryInstance.setStatus(status);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void initQueryInstance(QueryInstanceInitRequest initRequest) {
        if (queryInstanceRepo.existByIp(initRequest.getQueryIpAddress()))
            throw new QueryInstanceAlreadyInitiatedException();
        if (!isPortRangeMatchSlots(initRequest))
            throw new InstancePortRangeNotValidException("given port range dos not enough for specified maxInstance slot.");

        ServerQueryCredentials credentials = new ServerQueryCredentials(
                initRequest.getQueryIpAddress(),
                initRequest.getQueryPort(),
                initRequest.getQueryUsername(),
                initRequest.getQueryPassword()
        );

        QueryInstance queryInstance = QueryInstance.builder()
                .credentials(credentials)
                .startPort(initRequest.getStartPort())
                .stopPort(initRequest.getStopPort())
                .maxTeaSpeakInstance(initRequest.getMaxTeaSpeakInstance())
                .status(QueryInstanceStatus.INITIALIZING)
                .enabled(initRequest.isEnabled())
                .build();

        queryInstanceRepo.save(queryInstance);
        addInstanceToPool(queryInstance);
    }

    public QueryInstance getAvailableQueryInstance() {
        QueryInstance queryInstance = strategyHandler.getProviderQueryInstance();
        log.info("Selected query instance is (ID={} - HOST={}:{}) by {} Strategy",
                queryInstance.getId(),
                queryInstance.getCredentials().ip(),
                queryInstance.getCredentials().port(),
                strategyHandler.getType().name()
        );
        return queryInstance;
    }

    @Async
    protected void addInstanceToPool(QueryInstance queryInstance) {
        final Long instanceId = queryInstance.getId();
        final ServerQueryCredentials credentials = queryInstance.getCredentials();

        try {
            connectionPool.addConnection(credentials);
            if (queryInstance.isEnabled())
                changeQueryInstanceStatus(instanceId, QueryInstanceStatus.DISPATCHED);
            else
                changeQueryInstanceStatus(instanceId, QueryInstanceStatus.READY);

        } catch (QueryConnectionPoolingException e) {
            changeQueryInstanceStatus(instanceId, QueryInstanceStatus.UNREACHABLE);
            log.error("failed adding new connection with credentials: {}:{} - {}:{}",
                    credentials.ip(), credentials.port(), credentials.username(), credentials.password(), e);
        } catch (QueryLoginFailedException e) {
            changeQueryInstanceStatus(instanceId, QueryInstanceStatus.LOGIN_FAILED);
            log.error(e.getMessage());
        }
    }

    @Transactional
    public List<QueryInstanceListResponse> getAllQueryInstance() {
        List<QueryInstanceListResponse> queryInstanceListResponse = queryInstanceRepo.findAll()
                .stream()
                .map(q -> {
                    QueryInstanceListResponse response = modelMapper.map(q, QueryInstanceListResponse.class);
                    response.setUsedInstanceSlot(q.getInstances().size());
                    return response;
                })
                .toList();

        if (queryInstanceListResponse.isEmpty())
            throw new NoSuchDataException("No query instance found.");

        return queryInstanceListResponse;
    }



}

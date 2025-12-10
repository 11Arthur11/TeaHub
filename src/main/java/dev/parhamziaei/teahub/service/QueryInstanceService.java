package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.dto.response.teaspeak.QueryInstanceListResponse;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.InstancePortRangeNotValidException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.QueryInstanceAlreadyInitiatedException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.QueryInstanceNotFoundException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.TelnetConnectionPool;
import dev.parhamziaei.teahub.enums.QueryInstanceStatus;
import dev.parhamziaei.teahub.integration.teaspeak_query.internal_service.ProvisionStrategyFactory;
import dev.parhamziaei.teahub.integration.teaspeak_query.internal_service.TeaSpeakProvisionStrategyHandler;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
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
    private final MessageService messageService;

    public QueryInstanceService(
            QueryInstanceRepository queryInstanceRepo,
            TelnetConnectionPool connectionPool,
            QueryInstanceProperties queryInstanceProperties,
            ProvisionStrategyFactory provisionStrategyFactory,
            ModelMapper modelMapper,
            MessageService messageService
    ) {
        this.queryInstanceRepo = queryInstanceRepo;
        this.connectionPool = connectionPool;
        this.queryInstanceProperties = queryInstanceProperties;
        this.strategyHandler = provisionStrategyFactory.getStrategy();
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }

    public QueryInstance loadQueryInstance(Long id) {
        return queryInstanceRepo.findById(id)
                .orElseThrow(QueryInstanceNotFoundException::new);
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
        queryInstanceRepo.update(queryInstance);
    }

    public void changeQueryInstanceStatus(String ip, Integer port, QueryInstanceStatus status) {
        QueryInstance queryInstance = queryInstanceRepo.findByAddress(ip, port)
                .orElseThrow(QueryInstanceNotFoundException::new);
        queryInstance.setStatus(status);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void initiateQueryInstance(QueryInstanceInitRequest initRequest) {
        if (queryInstanceRepo.existByAddress(initRequest.getQueryIpAddress(), initRequest.getQueryPort()))
            throw new QueryInstanceAlreadyInitiatedException();
        if (!isPortRangeMatchSlots(initRequest))
            throw new InstancePortRangeNotValidException("given port range dos not enough for specified maxInstance slot.");

        ServerQueryCredentials credentials = new ServerQueryCredentials(
                initRequest.getQueryIpAddress(),
                initRequest.getQueryPort(),
                initRequest.getQueryUsername(),
                initRequest.getQueryPassword()
        );

        List<TeaSpeakResource> instances = new ArrayList<>();
        QueryInstance queryInstance = QueryInstance.builder()
                .name(initRequest.getName())
                .credentials(credentials)
                .defaultQueryServerGroupId(initRequest.getDefaultQueryServerGroupId())
                .startPort(initRequest.getStartPort())
                .stopPort(initRequest.getStopPort())
                .maxTeaSpeakInstance(initRequest.getMaxTeaSpeakInstance())
                .instances(instances)
                .build();

        queryInstanceRepo.save(queryInstance);
        if (initRequest.isEnabled())
            dispatchQueryInstance(queryInstance);
        else
            queryInstance.setStatus(QueryInstanceStatus.DISABLED);
    }

    @Transactional
    @EventListener(ContextClosedEvent.class)
    public void gracefulShutdown() {
        queryInstanceRepo.findByStatus(QueryInstanceStatus.DISPATCHED)
                .forEach(q -> {
                    log.info("Shutdown-Operation -> Instance ({} with address: {}) shutting down...",
                            q.getName(), q.getAddress());
                    connectionPool.removeConnection(q.getCredentials());
                    q.setStatus(QueryInstanceStatus.INITIATED);
                });
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRuntimePooling() {
        // note: getting dispatched instances in case of ungraceful shutdown.
        queryInstanceRepo.findAll()
                .stream().filter(q -> q.getStatus() == QueryInstanceStatus.DISPATCHED || q.getStatus() == QueryInstanceStatus.INITIATED)
                .forEach(queryInstance -> {
                    log.info("Initialization-Operation -> Initializing ({} with address: {}) query instance...",
                            queryInstance.getName(),  queryInstance.getAddress());
                    dispatchQueryInstance(queryInstance);
                });
    }

    @Transactional
    public QueryInstance getAvailableQueryInstance() {
        QueryInstance queryInstance = strategyHandler.getProviderQueryInstance();
        log.info("Provision-Operation -> Selected query instance is (ID={} - HOST={}:{}) by {} Strategy",
                queryInstance.getId(),
                queryInstance.getCredentials().ip(),
                queryInstance.getCredentials().port(),
                strategyHandler.getType().name()
        );
        return queryInstance;
    }

    public void dispatchQueryInstance(Long id) {
        dispatchQueryInstance(loadQueryInstance(id));
    }

    @Async
    public void dispatchQueryInstance(QueryInstance queryInstance) {
        connectionPool.addConnection(queryInstance.getCredentials());
        changeQueryInstanceStatus(queryInstance.getId(), QueryInstanceStatus.DISPATCHED);
    }

    public void disableQueryInstance(Long id) {
        QueryInstance queryInstance = loadQueryInstance(id);
        connectionPool.removeConnection(queryInstance.getCredentials());
        queryInstance.setStatus(QueryInstanceStatus.DISABLED);
        queryInstanceRepo.update(queryInstance);
    }

    @Transactional
    public List<QueryInstanceListResponse> getAllQueryInstance() {
        List<QueryInstanceListResponse> queryInstanceListResponse = queryInstanceRepo.findAll()
                .stream()
                .map(q -> {
                    QueryInstanceListResponse response = modelMapper.map(q, QueryInstanceListResponse.class);
                    response.setUsedInstanceSlot(q.getInstances().size());
                    response.setStatus(messageService.get(q.getStatus()));
                    return response;
                })
                .toList();

        if (queryInstanceListResponse.isEmpty())
            throw new NoSuchDataException("No query instance found.");

        return queryInstanceListResponse;
    }

    public void removeQueryInstance(Long id) {
        QueryInstance instance = loadQueryInstance(id);
        connectionPool.removeConnection(instance.getCredentials());
        queryInstanceRepo.delete(instance);
    }



}

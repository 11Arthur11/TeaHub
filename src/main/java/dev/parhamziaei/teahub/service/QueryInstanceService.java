package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.InstancePortRangeNotValidException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.QueryInstanceAlreadyInitiatedException;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.TelnetConnectionPool;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.QueryInstanceStatus;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryInstanceService {

    private final QueryInstanceRepository yatqaRepository;
    private final TelnetConnectionPool connectionPool;
    private final QueryInstanceProperties queryInstanceProperties;

    private boolean isPortRangeMatchSlots(QueryInstanceInitRequest request) {
        final int givenPortCount = request.getStopPort() - request.getStartPort();
        if (!(givenPortCount % queryInstanceProperties.portStep() == 0)) {
            throw new InstancePortRangeNotValidException("port range dos not match the port step.");
        }
        return (givenPortCount / queryInstanceProperties.portStep()) == request.getMaxTeaSpeakInstance();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void initQueryInstance(QueryInstanceInitRequest initRequest) {
        if (yatqaRepository.existByIp(initRequest.getQueryIpAddress()))
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
                .endPort(initRequest.getStopPort())
                .maxVM(initRequest.getMaxTeaSpeakInstance())
                .status(QueryInstanceStatus.INITIALIZING)
                .enabled(initRequest.isEnabled())
                .build();

        yatqaRepository.save(queryInstance);
        connectionPool.addConnection(credentials);
    }



}

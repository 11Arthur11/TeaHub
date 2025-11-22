package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.YatqaNotEnoughPortsException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.YatqaServerAlreadyInitiatedException;
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

    @PreAuthorize("hasRole('ADMIN')")
    public void initQueryInstance(QueryInstanceInitRequest initRequest) {
        if (yatqaRepository.existByIp(initRequest.getYatqaIp()))
            throw new YatqaServerAlreadyInitiatedException();
        if (initRequest.getEndPort() - initRequest.getStartPort() < initRequest.getMaxVM())
            throw new YatqaNotEnoughPortsException();

        ServerQueryCredentials credentials = new ServerQueryCredentials(
                initRequest.getYatqaIp(),
                initRequest.getYatqaPort(),
                initRequest.getYatqaUsername(),
                initRequest.getYatqaPassword()
        );

        QueryInstance queryInstance = QueryInstance.builder()
                .credentials(credentials)
                .startPort(initRequest.getStartPort())
                .endPort(initRequest.getEndPort())
                .maxVM(initRequest.getMaxVM())
                .status(QueryInstanceStatus.INITIALIZING)
                .enabled(initRequest.isEnabled())
                .build();

        yatqaRepository.save(queryInstance);
        connectionPool.addConnection(credentials);
    }



}

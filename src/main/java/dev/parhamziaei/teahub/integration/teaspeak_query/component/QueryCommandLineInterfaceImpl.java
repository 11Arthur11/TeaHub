package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.TelnetSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryCommandLineInterfaceImpl implements QueryCLI{

    private final TelnetConnectionPool connectionPool;
    private final QueryCommandFactory commandFactory;
    private final ModelMapper modelMapper;

    @Override
    public TSCreateQueryResponse createServer(ServerQueryCredentials credentials, TSCreateQueryRequest createRequest) {
        final String command = commandFactory.createServerCommand(createRequest);
        TelnetSession session = connectionPool.getSession(credentials);
        String rawResponse = session.sendCommand(command);
        return modelMapper.map(
                ResponseDecoder.convertToMap(rawResponse),
                TSCreateQueryResponse.class
        );
    }

}

package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeAddResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeListResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSServerInfoResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.TelnetSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryCommandLineInterfaceImpl implements QueryCLI{

    private final TelnetConnectionPool connectionPool;
    private final QueryCommandFactory commandFactory;
    private final ModelMapper modelMapper;

    @Override
    public TSCreateQueryResponse createServer(ServerQueryCredentials credentials, TSCreateQueryRequest createRequest) {
        final String command = commandFactory.createServerCommand(createRequest);
        TelnetSession session = connectionPool.borrow(credentials);
        String rawResponse = session.execute(command);
        connectionPool.returnToPool(session);
        log.debug("raw response {}", rawResponse);
        return modelMapper.map(
                ResponseDecoder.convertToMap(rawResponse),
                TSCreateQueryResponse.class
        );
    }

    @Override
    public TSPrivilegeAddResponse generateNewQueryPrivilegeToken(ServerQueryCredentials credentials, String sid, String serverGroupId) {
        final String selectCommand = commandFactory.useCommand(sid);
        final String generatePrivilegeCommand = commandFactory.generatePrivilegeCommand(serverGroupId);
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(selectCommand);
        String rawResponse = session.execute(generatePrivilegeCommand);
        connectionPool.returnToPool(session);
        return modelMapper.map(
                ResponseDecoder.convertToMap(rawResponse),
                TSPrivilegeAddResponse.class
        );
    }

    @Override
    public List<TSPrivilegeListResponse> getPrivilegeTokens(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(commandFactory.useCommand(sid));
        String rawResponse = session.execute(commandFactory.privilegeListCommand());
        connectionPool.returnToPool(session);
        return ResponseDecoder.convertMultiPipeToMap(rawResponse)
                .stream()
                .map(map -> modelMapper.map(map, TSPrivilegeListResponse.class))
                .toList();
    }

    @Override
    public void startServer(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(commandFactory.startCommand(sid));
        connectionPool.returnToPool(session);
    }

    @Override
    public void stopServer(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(commandFactory.stopCommand(sid));
        connectionPool.returnToPool(session);
    }

    @Override
    public TSServerInfoResponse getServerInfo(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(commandFactory.useCommand(sid));
        String rawResponse = session.execute(commandFactory.serverInfoCommand());
        connectionPool.returnToPool(session);
        return modelMapper.map(ResponseDecoder.convertToMap(rawResponse), TSServerInfoResponse.class);
    }


}

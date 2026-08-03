package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
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
        try {
            String rawResponse = session.execute(command);
            return modelMapper.map(
                    ResponseDecoder.convertToMap(rawResponse),
                    TSCreateQueryResponse.class
            );
        } finally {
            connectionPool.returnToPool(session);
        }
    }

    @Override
    public void deleteServer(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        try {
            session.execute(commandFactory.deleteServerCommand(sid));
        } finally {
            connectionPool.returnToPool(session);
        }
    }

    @Override
    public TSPrivilegeAddResponse generatePrivilegeToken(ServerQueryCredentials credentials, String sid, String serverGroupId) {
        final String selectCommand = commandFactory.useCommand(sid);
        final String generatePrivilegeCommand = commandFactory.generatePrivilegeCommand(serverGroupId);
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(selectCommand);
        try {
            String rawResponse = session.execute(generatePrivilegeCommand);
            return modelMapper.map(
                    ResponseDecoder.convertToMap(rawResponse),
                    TSPrivilegeAddResponse.class
            );
        } finally {
            connectionPool.returnToPool(session);
        }

    }

    @Override
    public List<TSPrivilegeListResponse> getPrivilegeTokens(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(commandFactory.useCommand(sid));
        try {
            String rawResponse = session.execute(commandFactory.privilegeListCommand());
            return ResponseDecoder.convertMultiPipeToMap(rawResponse)
                    .stream()
                    .map(map -> modelMapper.map(map, TSPrivilegeListResponse.class))
                    .toList();
        } finally {
            connectionPool.returnToPool(session);
        }
    }

    @Override
    public void startServer(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        try {
            session.execute(commandFactory.startCommand(sid));
        } finally {
            connectionPool.returnToPool(session);
        }
    }

    @Override
    public void stopServer(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        try {
            session.execute(commandFactory.stopCommand(sid));
        } finally {
            connectionPool.returnToPool(session);
        }
    }

    @Override
    public TSServerInfoResponse getServerInfo(ServerQueryCredentials credentials, String sid) {
        TelnetSession session = connectionPool.borrow(credentials);
        session.execute(commandFactory.useCommand(sid));
        try {
            String rawResponse = session.execute(commandFactory.serverInfoCommand());
            return modelMapper.map(ResponseDecoder.convertToMap(rawResponse), TSServerInfoResponse.class);
        } finally {
            connectionPool.returnToPool(session);
        }
    }

    @Override
    public void deletePrivilegeToken(ServerQueryCredentials credentials, String sid, String token) {
        TelnetSession session = connectionPool.borrow(credentials);
        try {
            session.execute(commandFactory.useCommand(sid));
            session.execute(commandFactory.deletePrivilegeCommand(token));
        } finally {
            connectionPool.returnToPool(session);
        }
    }


}

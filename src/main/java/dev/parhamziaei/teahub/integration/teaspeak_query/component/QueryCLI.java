package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeAddResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeListResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSServerInfoResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;

import java.util.List;

public interface QueryCLI {

    TSCreateQueryResponse createServer(ServerQueryCredentials credentials, TSCreateQueryRequest createRequest);
    TSPrivilegeAddResponse generatePrivilegeToken(ServerQueryCredentials credentials, String sid, String serverGroupId);
    List<TSPrivilegeListResponse> getPrivilegeTokens(ServerQueryCredentials credentials, String sid);
    void startServer(ServerQueryCredentials credentials, String sid);
    void stopServer(ServerQueryCredentials credentials, String sid);
    TSServerInfoResponse getServerInfo(ServerQueryCredentials credentials, String sid);
    void deletePrivilegeToken(ServerQueryCredentials credentials, String sid, String token);
    void deleteServer(ServerQueryCredentials credentials, String sid);

}

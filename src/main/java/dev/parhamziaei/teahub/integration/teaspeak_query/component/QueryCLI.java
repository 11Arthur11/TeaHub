package dev.parhamziaei.teahub.integration.teaspeak_query.component;

import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeAddResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;

public interface QueryCLI {

    TSCreateQueryResponse createServer(ServerQueryCredentials credentials, TSCreateQueryRequest createRequest);
    TSPrivilegeAddResponse generateNewQueryPrivilegeToken(ServerQueryCredentials credentials, String sid, String serverGroupId);

}

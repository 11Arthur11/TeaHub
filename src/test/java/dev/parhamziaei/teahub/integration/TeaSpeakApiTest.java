package dev.parhamziaei.teahub.integration;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServer;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.component.QueryCLI;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.request.TSCreateQueryRequest;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSCreateQueryResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSPrivilegeAddResponse;
import dev.parhamziaei.teahub.service.QueryInstanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
public class TeaSpeakApiTest {

//    @Autowired
//    private QueryCLI queryCLI;
//    @Autowired
//    private QueryInstanceService queryInstanceService;
//
//    @Test
//    public void testCreateVirtualServer() {
//        long start = System.currentTimeMillis();
//
//        final TS3Config config = new TS3Config();
//        config.setHost("92.114.50.116");
//        config.setQueryPort(1220);
//
//        final TS3Query query = new TS3Query(config);
//        query.connect();
//
//        Map<VirtualServerProperty, String> properties = new HashMap<>();
//        properties.put(VirtualServerProperty.VIRTUALSERVER_PORT, "5585");
//        properties.put(VirtualServerProperty.VIRTUALSERVER_NAME, "test");
//        properties.put(VirtualServerProperty.VIRTUALSERVER_MAXCLIENTS, "80");
//
//
//        final TS3Api api = query.getApi();
//        api.login("serveradmin", "v7oJrLGnixsG");
//        api.createServer("mytestserver", properties);
//
//        long end = System.currentTimeMillis();
//        System.out.println("Execution took " + (end - start) + " ms");
//    }
//
//    @Test
//    public void createVirtualServerTest_CustomModule() {
//        TSCreateQueryRequest createRequest = TSCreateQueryRequest.builder()
//                .maxClients(String.valueOf(50))
//                .port(String.valueOf(5585))
//                .serverName(
//                        generateInstanceName(
//                                "test",
//                                2L
//                        )
//                ).build();
//
//        QueryInstance queryInstance = queryInstanceService.getAvailableQueryInstance();
//
//        long start = System.currentTimeMillis();
//        TSCreateQueryResponse createServerResponse = queryCLI.createServer(queryInstance.getCredentials(), createRequest);
//        TSPrivilegeAddResponse privilegeAddResponse = queryCLI.generatePrivilegeToken(
//                queryInstance.getCredentials(),
//                createServerResponse.getSid(),
//                String.valueOf(queryInstance.getDefaultQueryServerGroupId())
//        );
//
//        long end = System.currentTimeMillis();
//        System.out.println("Execution took " + (end - start) + " ms");
//    }
//
//    private String generateInstanceName(String label, Long resourceId) {
//        return label.replace(" ", "\\s") + "\\s-\\sResourceID:\\s" + String.format("%06d", resourceId);
//    }

}

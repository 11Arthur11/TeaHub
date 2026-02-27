package dev.parhamziaei.teahub.integration;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TeaSpeakApiTest {

    @Test
    public void testCreateVirtualServer() {
        long start = System.currentTimeMillis();

        final TS3Config config = new TS3Config();
        config.setHost("92.114.50.116");
        config.setQueryPort(1220);

        final TS3Query query = new TS3Query(config);
        query.connect();

        Map<VirtualServerProperty, String> properties = new HashMap<>();
        properties.put(VirtualServerProperty.VIRTUALSERVER_PORT, "5585");
        properties.put(VirtualServerProperty.VIRTUALSERVER_NAME, "test");
        properties.put(VirtualServerProperty.VIRTUALSERVER_MAXCLIENTS, "80");


        final TS3Api api = query.getApi();
        api.login("serveradmin", "v7oJrLGnixsG");
        api.createServer("mytestserver", properties);

        long end = System.currentTimeMillis();
        System.out.println("Execution took " + (end - start) + " ms");
    }

}

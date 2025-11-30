package dev.parhamziaei.teahub.kafka.handler;

import dev.parhamziaei.teahub.integration.teaspeak_query.component.TelnetConnectionPool;
import dev.parhamziaei.teahub.enums.QueryInstanceStatus;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionLoginFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionReviveFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionUnreachableEvent;
import dev.parhamziaei.teahub.service.QueryInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelnetEventHandler {

    private final QueryInstanceService queryInstanceService;
    private final TelnetConnectionPool telnetConnectionPool;

    public void handleUnreachableEvent(TelnetSessionUnreachableEvent event) {
        queryInstanceService.changeQueryInstanceStatus(event.getIp(), event.getPort(), QueryInstanceStatus.RECONNECTING);
        telnetConnectionPool.reconnect(event.getCredentials());

        // reminder: notif admins here
    }

    public void handleLoginFailedEvent(TelnetSessionLoginFailedEvent event) {
        queryInstanceService.changeQueryInstanceStatus(event.getIp(), event.getPort(), QueryInstanceStatus.LOGIN_FAILED);

        // reminder: notif admins here
    }

    public void handleReviveFailedEvent(TelnetSessionReviveFailedEvent event) {
        queryInstanceService.changeQueryInstanceStatus(event.getIp(), event.getPort(), QueryInstanceStatus.UNREACHABLE);
        // reminder: notif admins here
    }

}

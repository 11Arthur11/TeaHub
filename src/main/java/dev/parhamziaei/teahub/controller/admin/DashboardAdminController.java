package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.AudioBotNodeService;
import dev.parhamziaei.teahub.service.QueryInstanceService;
import dev.parhamziaei.teahub.service.ResourceService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.service.interfaces.TicketService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/dashboard")
@RequiredArgsConstructor
public class DashboardAdminController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final ResourceService resourceService;
    private final QueryInstanceService queryInstanceService;
    private final AudioBotNodeService audioBotNodeService;
    private final TicketService ticketService;

    @GetMapping("/overview")
    public ResponseEntity<DataResponse<AdminMetric>> overview() {
        AdminMetric overviewResponse = AdminMetric.builder()
                .financeMetric(paymentService.financeMetric())
                .userMetric(userService.userMetric())
                .resourceMetric(resourceService.getResourceMetric())
                .queryInstanceMetric(queryInstanceService.getNodeMetric())
                .audioBotNodeMetric(audioBotNodeService.getNodeMetric())
                .ticketMetric(ticketService.ticketMetric())
                .build();

        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                overviewResponse,
                HttpStatus.OK
        );
    }


}

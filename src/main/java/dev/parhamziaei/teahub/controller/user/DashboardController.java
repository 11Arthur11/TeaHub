package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.response.dashboard.user.DashboardOverviewResponse;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.ResourceService;
import dev.parhamziaei.teahub.service.interfaces.TicketService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ResourceService resourceService;
    private final TicketService ticketService;
    private final CurrentUser currentUser;

    @GetMapping("/overview")
    public ResponseEntity<DataResponse<DashboardOverviewResponse>> getDashboardOverviewResponse() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                new DashboardOverviewResponse(
                        resourceService.getResourceOverview(currentUser.getId()),
                        ticketService.countOpenTickets(currentUser.getId())
                ),
                HttpStatus.OK
        );
    }

}

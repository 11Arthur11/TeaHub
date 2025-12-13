package dev.parhamziaei.teahub.schedule.invoice;

import dev.parhamziaei.teahub.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceGenerateSchedule {

    private final InvoiceService invoiceService;

    @Scheduled(cron = "0 0 12 * * *")

}

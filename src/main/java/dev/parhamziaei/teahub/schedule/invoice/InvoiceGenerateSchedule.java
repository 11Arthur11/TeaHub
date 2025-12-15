package dev.parhamziaei.teahub.schedule.invoice;

import dev.parhamziaei.teahub.entity.jpa.ApplicationSetting;
import dev.parhamziaei.teahub.repository.jpa.ApplicationSettingRepository;
import dev.parhamziaei.teahub.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class InvoiceGenerateSchedule {

//    @Scheduled(cron = "0 0 * * * *")
//    public void generateMonthlyResourceInvoice() {
//
//
//
//    }

}

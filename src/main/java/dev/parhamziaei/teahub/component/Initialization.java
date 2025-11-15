package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.integration.ippanel.IPPanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initialization implements CommandLineRunner {

    private final IPPanelService ipPanelService;

    @Override
    public void run(String... args) throws Exception {
        ipPanelService.sendTwoFactor("0000", "+989053781183");
    }



}

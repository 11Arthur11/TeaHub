package dev.parhamziaei.teahub;

import dev.parhamziaei.teahub.utils.UriUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeaHubApplicationTests {


    @Test
    void contextLoads() {
        String rawLink = "https://dl.musictaj.com/song403/bhr/Siyam%20x%20Reza%20Pishro%20x%20Shayea%20x%20Ali%20Sorena%20x%20Putak%20-%20Poochi%20(Remix).mp3";
        String encoded = UriUtils.encodeURIComponent(rawLink)
                .replace("(", "%28")
                .replace(")", "%29");
        System.out.println(encoded);
    }

}

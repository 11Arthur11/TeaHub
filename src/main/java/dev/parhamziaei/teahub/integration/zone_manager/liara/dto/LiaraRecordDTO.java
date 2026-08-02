package dev.parhamziaei.teahub.integration.zone_manager.liara.dto;

import dev.parhamziaei.teahub.enums.dns.DnsRecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiaraRecordDTO {

    private String name;
    private DnsRecordType type;
    private Integer ttl;
    private List<Content> contents;

    public Content getContents() {
        return contents.getFirst();
    }

    @Data
    @Builder
    public static class Content {

        private String ip;
        private String host;
        private Integer port;
        private Integer priority;
        private Integer weight;

    }

}

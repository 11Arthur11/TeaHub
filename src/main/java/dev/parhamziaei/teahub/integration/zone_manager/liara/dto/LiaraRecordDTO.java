package dev.parhamziaei.teahub.integration.zone_manager.liara.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.parhamziaei.teahub.enums.dns.DnsRecordType;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiaraRecordDTO {

    private String name;
    private DnsRecordType type;
    private Integer ttl;
    private List<Content> contents;

    @JsonIgnore
    public Content getContent() {
        return contents.getFirst();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {

        private String ip;
        private String host;
        private Integer port;
        private Integer priority;
        private Integer weight;

        public Content(String ip) {
            this.ip = ip;
        }
    }

}

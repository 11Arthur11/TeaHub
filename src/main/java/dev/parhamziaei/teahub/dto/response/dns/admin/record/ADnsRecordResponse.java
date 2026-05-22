package dev.parhamziaei.teahub.dto.response.dns.admin.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ADnsRecordResponse extends AbstractDnsRecordResponse {

    private String ip;

}

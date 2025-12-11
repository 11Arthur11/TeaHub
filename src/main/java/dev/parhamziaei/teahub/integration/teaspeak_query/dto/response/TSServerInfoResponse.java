package dev.parhamziaei.teahub.integration.teaspeak_query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TSServerInfoResponse extends BaseQueryResponse {

    private String virtualserver_name;
    private String virtualserver_maxclients;
    private String virtualserver_clientsonline;
    private String virtualserver_id;
    private String virtualserver_port;
    private String virtualserver_status;

}

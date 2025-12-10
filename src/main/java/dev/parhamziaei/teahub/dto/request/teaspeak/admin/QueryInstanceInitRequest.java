package dev.parhamziaei.teahub.dto.request.teaspeak.admin;

import dev.parhamziaei.teahub.validation.annotation.IpAddress;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class QueryInstanceInitRequest {

    @NotEmpty
    private String name;

    @IpAddress
    private String queryIpAddress;

    @Range(min = 1, max = 65535)
    private Integer queryPort;

    @NotNull
    private String queryUsername;

    @NotEmpty
    private String queryPassword;

    @NotNull
    private Integer defaultQueryServerGroupId;

    @NotNull
    private Integer maxTeaSpeakInstance;

    @Range(min = 1, max = 65535)
    private Integer startPort;

    @Range(min = 1, max = 65535)
    private Integer stopPort;

    @NotNull
    private boolean enabled;

}

package dev.parhamziaei.teahub.dto.request.teaspeak.admin;

import dev.parhamziaei.teahub.validation.annotation.IpAddress;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class QueryInstanceInitRequest {

    @IpAddress
    private String queryIpAddress;

    @NotEmpty
    @Length(min = 1, max = 5)
    private Integer queryPort;

    @NotEmpty
    private String queryUsername;

    @NotEmpty
    private String queryPassword;

    @NotEmpty
    private Integer maxTeaSpeakInstance;

    @NotEmpty
    @Length(min = 1, max = 5)
    private Integer startPort;

    @Length(min = 1, max = 5)
    private Integer stopPort;

    @NotEmpty
    private boolean enabled;

}

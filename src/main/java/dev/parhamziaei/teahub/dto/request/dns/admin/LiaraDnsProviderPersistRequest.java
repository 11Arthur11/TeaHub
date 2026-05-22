package dev.parhamziaei.teahub.dto.request.dns.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiaraDnsProviderPersistRequest {

    @NotBlank
    @NotNull
    private String baseUrl;

    @NotBlank
    @NotNull
    private String apiKey;

    private boolean active = false;
//    private Integer apiVersion;

}

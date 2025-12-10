package dev.parhamziaei.teahub.dto.response.resource.teaspeak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeaSpeakResourceTokenResponse {

    private Long id;
    private String token;

}

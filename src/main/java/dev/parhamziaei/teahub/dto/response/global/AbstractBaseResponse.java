package dev.parhamziaei.teahub.dto.response.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractBaseResponse {

    @Schema(description = "Operation success state, boolean")
    private boolean success;

    @Schema(description = "Operation Type", examples = {"SUCCESS", "ERROR", "PROCESSING", "FAILURE"})
    private String type;
}

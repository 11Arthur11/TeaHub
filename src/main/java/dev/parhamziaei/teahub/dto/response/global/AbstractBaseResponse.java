package dev.parhamziaei.teahub.dto.response.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractBaseResponse {
    private boolean success;
    private String type;
}

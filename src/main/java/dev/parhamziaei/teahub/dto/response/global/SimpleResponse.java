package dev.parhamziaei.teahub.dto.response.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "SimpleResponse, the type of responses with no data"
)
public class SimpleResponse extends AbstractBaseResponse{

    public SimpleResponse(boolean success, String type, String message) {
        super(success, type);
        this.message = message;
    }

    @Schema(description = "Message of response, always translated to farsi")
    private String message;

}

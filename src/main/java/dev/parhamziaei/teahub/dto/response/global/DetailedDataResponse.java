package dev.parhamziaei.teahub.dto.response.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "DetailedDataResponse, the type of responses with data and message"
)
public class DetailedDataResponse<T> extends AbstractBaseResponse {

    public DetailedDataResponse(boolean success, String type, String message, T data) {
        super(success, type);
        this.data = data;
        this.message = message;
    }

    private T data;
    private String message;
}

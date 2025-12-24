package dev.parhamziaei.teahub.dto.response.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "DataResponse, the type of responses with data only"
)
public class DataResponse<T> extends AbstractBaseResponse {

    public DataResponse(boolean success, String type, T data) {
        super(success, type);
        this.data = data;
    }

    private T data;
}

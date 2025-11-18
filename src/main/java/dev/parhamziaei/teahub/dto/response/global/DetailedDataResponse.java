package dev.parhamziaei.teahub.dto.response.global;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailedDataResponse<T> extends AbstractBaseResponse {

    public DetailedDataResponse(boolean success, String type, String message, T data) {
        super(success, type);
        this.data = data;
        this.message = message;
    }

    private T data;
    private String message;
}

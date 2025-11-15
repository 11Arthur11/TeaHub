package dev.parhamziaei.teahub.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse<T> extends AbstractBaseResponse {

    public DataResponse(boolean success, String type, T data) {
        super(success, type);
        this.data = data;
    }

    private T data;
}

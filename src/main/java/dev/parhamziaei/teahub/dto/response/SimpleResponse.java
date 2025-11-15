package dev.parhamziaei.teahub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleResponse extends AbstractBaseResponse{

    public SimpleResponse(boolean success, String type, String message) {
        super(success, type);
        this.message = message;
    }

    private String message;

}

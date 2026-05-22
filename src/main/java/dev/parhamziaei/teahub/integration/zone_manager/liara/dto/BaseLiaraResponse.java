package dev.parhamziaei.teahub.integration.zone_manager.liara.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseLiaraResponse<T> {

    private String status;
    private T data;

}

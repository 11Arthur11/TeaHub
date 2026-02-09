package dev.parhamziaei.teahub.dto.request.resource.user;

import lombok.Data;

@Data
public class BillableResourceEditRequest {

    private String label;

    private boolean autoProlong;

}

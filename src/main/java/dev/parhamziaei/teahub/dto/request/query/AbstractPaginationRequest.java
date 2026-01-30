package dev.parhamziaei.teahub.dto.request.query;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPaginationRequest {
    protected int page = 0;
    protected int size = 20;

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}

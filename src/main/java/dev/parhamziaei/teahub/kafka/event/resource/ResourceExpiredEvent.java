package dev.parhamziaei.teahub.kafka.event.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceExpiredEvent {

    private Long resourceId;

}

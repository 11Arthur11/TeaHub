package dev.parhamziaei.teahub.kafka.event.resource;

import dev.parhamziaei.teahub.dto.request.resource.user.NewAudioBotResourceRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioBotDeployEvent {

    private Long productId;
    private Long resourceId;
    private NewAudioBotResourceRequest resourceRequest;

}

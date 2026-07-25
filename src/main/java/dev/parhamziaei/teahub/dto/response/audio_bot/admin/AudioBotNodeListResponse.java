package dev.parhamziaei.teahub.dto.response.audio_bot.admin;

import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.enums.audio_bot.NodeStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AudioBotNodeListResponse {

    private Long id;

    private String name;

    private String webAddress;

    private NodeStatus nodeStatus;

    private Integer maxBotInstance;

    private boolean enabled;

}

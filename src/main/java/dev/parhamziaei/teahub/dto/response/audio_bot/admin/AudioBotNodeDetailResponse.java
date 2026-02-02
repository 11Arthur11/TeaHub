package dev.parhamziaei.teahub.dto.response.audio_bot.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AudioBotNodeDetailResponse {

    private Long id;

    private String name;

    private String webAddress;

    private String username;

    private String password;

    private String nodeStatus;

    private LocalDateTime initiatedAt;

    private LocalDateTime lastUsed;

    private Integer maxBotInstance;

    private Integer onlineInstanceCount;

    private Integer allInstanceCount;

    private boolean isFull;

    private boolean enabled;

}

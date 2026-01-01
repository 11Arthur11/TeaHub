package dev.parhamziaei.teahub.entity.jpa.audio_bot;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.enums.audio_bot.NodeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioBotNode extends BaseEntity<Long> {

    private String name;

    @Column(unique = true)
    private String webAddress;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private NodeStatus nodeStatus;

    @Column(columnDefinition = "TIMESTAMP(0)", updatable = false)
    private LocalDateTime initiatedAt;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lastUsed;

    private Integer maxBotInstance;

    private boolean isFull;

    @OneToMany(mappedBy = "parentNode", fetch = FetchType.LAZY)
    private List<AudioBotResource> instances = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.initiatedAt = LocalDateTime.now().withNano(0);
        this.lastUsed = LocalDateTime.now().withNano(0);
        this.isFull = instances.size() >= maxBotInstance;
    }

}

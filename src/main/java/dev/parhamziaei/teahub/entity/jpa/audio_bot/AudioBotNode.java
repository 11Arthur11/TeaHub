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

    private Integer onlineInstanceCount;

    private Integer allInstanceCount;

    private boolean isFull;

    private boolean enabled;

    @OneToMany(mappedBy = "parentNode", fetch = FetchType.LAZY)
    private List<AudioBotResource> instances = new ArrayList<>();

    public boolean isAvailable() {
        return !isFull && enabled;
    }

    @Builder
    public AudioBotNode(
            String name,
            String webAddress,
            String username,
            String password,
            NodeStatus nodeStatus,
            Integer maxBotInstance,
            boolean enabled
    ) {
        this.name = name;
        this.webAddress = webAddress;
        this.username = username;
        this.password = password;
        this.maxBotInstance = maxBotInstance;
        this.instances = new ArrayList<>();
        this.enabled = enabled;
        this.nodeStatus = nodeStatus;
    }

    @PrePersist
    public void prePersist() {
        this.initiatedAt = LocalDateTime.now().withNano(0);
        this.lastUsed = LocalDateTime.now().withNano(0);
        this.isFull = instances.size() >= maxBotInstance;
    }

}

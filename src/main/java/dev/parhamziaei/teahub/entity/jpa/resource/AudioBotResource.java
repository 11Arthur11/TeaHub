package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("AUDIO_BOT")
@SuperBuilder
public class AudioBotResource extends BillableResource {

    private String identifier;

    private String botNickname;

    private String serverAddress;

    private String serverPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id")
    private AudioBotNode parentNode;

    @Enumerated(EnumType.STRING)
    private AudioBotStatus botStatus;

    public void setParentNode(AudioBotNode parentNode) {
        this.parentNode = parentNode;
        if (this.parentNode.getInstances() == null)
            this.parentNode.setInstances(new ArrayList<>());
        this.parentNode.getInstances().add(this);
    }

}

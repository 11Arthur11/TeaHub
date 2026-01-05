package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.root;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._use.AudioBotUseBuilder;

public class AudioBotBotBuilder {

    private final StringBuilder sb;

    public AudioBotBotBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotUseBuilder use(Long id) {
        sb.append("/use/").append(id);
        return new AudioBotUseBuilder(sb);
    }

    public AudioBotBuildStep connect(String name) {
        sb.append("/connect/template/").append(name);
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep list() {
        sb.append("/list");
        return new AudioBotBuildStep(sb);
    }

}

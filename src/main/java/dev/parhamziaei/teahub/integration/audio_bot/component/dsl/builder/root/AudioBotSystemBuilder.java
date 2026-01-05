package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.root;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;

public class AudioBotSystemBuilder {

    private final StringBuilder sb;

    public AudioBotSystemBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotBuildStep info() {
        sb.append("/info");
        return new AudioBotBuildStep(sb);
    }

}

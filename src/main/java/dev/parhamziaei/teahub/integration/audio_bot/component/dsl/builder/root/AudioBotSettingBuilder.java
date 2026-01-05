package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.root;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;

public class AudioBotSettingBuilder {

    private final StringBuilder sb;

    public AudioBotSettingBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotBuildStep create(String name) {
        sb.append("/create/").append(name);
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep delete(String name) {
        sb.append("/delete/").append(name);
        return new AudioBotBuildStep(sb);
    }

}

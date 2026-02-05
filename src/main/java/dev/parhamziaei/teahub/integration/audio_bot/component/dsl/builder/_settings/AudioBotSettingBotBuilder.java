package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._settings;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;

public class AudioBotSettingBotBuilder {
    private final StringBuilder sb;

    public AudioBotSettingBotBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotSetSettingParameterBuilder set(String name) {
        sb.append("/set/").append(name);
        return new AudioBotSetSettingParameterBuilder(sb);
    }

    public AudioBotBuildStep get(String name) {
        sb.append("/get/").append(name);
        return new AudioBotBuildStep(sb);
    }

}

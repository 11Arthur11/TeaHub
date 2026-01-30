package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.root;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._settings.AudioBotSettingBotBuilder;

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

    public AudioBotSettingBotBuilder bot() {
        sb.append("/bot");
        return new AudioBotSettingBotBuilder(sb);
    }


}

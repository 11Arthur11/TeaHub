package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder;


import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.root.AudioBotBotBuilder;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.root.AudioBotSettingBuilder;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.root.AudioBotSystemBuilder;

public class AudioBotRootBuilder {

    private final StringBuilder sb = new StringBuilder().append("/api");

    public AudioBotSettingBuilder settings() {
        sb.append("/settings");
        return new AudioBotSettingBuilder(sb);
    }

    public AudioBotSystemBuilder system() {
        sb.append("/system");
        return new AudioBotSystemBuilder(sb);
    }

    public AudioBotBotBuilder bot() {
        sb.append("/bot");
        return new AudioBotBotBuilder(sb);
    }

}

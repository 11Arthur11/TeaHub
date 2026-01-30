package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._settings;

public class AudioBotSettingBotBuilder {
    private final StringBuilder sb;

    public AudioBotSettingBotBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotSetSettingParameterBuilder set(String name) {
        sb.append("/set/").append(name);
        return new AudioBotSetSettingParameterBuilder(sb);
    }

}

package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._use;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;

public class AudioBotUseBuilder {

    private final StringBuilder sb;

    public AudioBotUseBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotBuildStep disconnect() {
        sb.append("/(/bot/disconnect)");
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep setConnectAddress(String host) {
        sb.append("/(/settings/set/connect.address/")
        .append(host)
        .append(")");
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep setConnectName(String name) {
        sb.append("/(/settings/set/connect.name/")
                .append(name)
                .append(")");
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep commander(boolean enabled) {
        sb.append("/(/bot/commander/")
        .append(enabled ? "on" : "off")
        .append(")");
        return new AudioBotBuildStep(sb);
    }

    public AudioBotPlayListCommandBuilder playlist() {
        sb.append("/(/list");
        return new AudioBotPlayListCommandBuilder(sb);
    }

}

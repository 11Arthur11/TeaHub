package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.AudioBotUri;

public class AudioBotBuildStep {

    private final StringBuilder sb;

    public AudioBotBuildStep(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotUri build() {
        return new AudioBotUri(sb.toString());
    }

}

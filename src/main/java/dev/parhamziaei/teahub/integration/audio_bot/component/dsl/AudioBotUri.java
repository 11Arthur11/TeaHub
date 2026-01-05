package dev.parhamziaei.teahub.integration.audio_bot.component.dsl;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotRootBuilder;
public class AudioBotUri {

    private final String uri;

    public AudioBotUri(String uri) {
        this.uri = uri;
    }

    public String value() {
        return uri;
    }

    public static AudioBotRootBuilder builder() {
        return new AudioBotRootBuilder();
    }

}

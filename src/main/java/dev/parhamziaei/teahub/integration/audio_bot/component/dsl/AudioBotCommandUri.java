package dev.parhamziaei.teahub.integration.audio_bot.component.dsl;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.BotNameStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.BuildStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.CreateStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.SettingStep;

public class AudioBotCommandUri {

    private final String uri;

    private AudioBotCommandUri(String uri) {
        this.uri = uri;
    }

    public String value() {
        return uri;
    }

    public static SettingStep builder() {
        return new Builder();
    }

    private static class Builder implements BotNameStep, BuildStep, CreateStep, SettingStep {

        private final StringBuilder sb = new StringBuilder().append("/api");

        @Override
        public BuildStep botName(String botName) {
            sb.append("/").append(botName);
            return this;
        }

        @Override
        public AudioBotCommandUri build() {
            return new AudioBotCommandUri(sb.toString());
        }

        @Override
        public BotNameStep create() {
            sb.append("/create");
            return this;
        }

        @Override
        public CreateStep setting() {
            sb.append("/setting");
            return this;
        }
    }
}

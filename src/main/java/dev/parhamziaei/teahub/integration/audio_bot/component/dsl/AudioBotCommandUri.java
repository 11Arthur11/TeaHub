package dev.parhamziaei.teahub.integration.audio_bot.component.dsl;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.BuildStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.RootStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.bot_group.BotStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.bot_group.PlayListStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.bot_group.UseStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.setting_group.CreateStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.setting_group.DeleteStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.setting_group.SettingStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.system_group.SystemStep;

public class AudioBotCommandUri {

    private final String uri;

    private AudioBotCommandUri(String uri) {
        this.uri = uri;
    }

    public String value() {
        return uri;
    }

    public static RootStep builder() {
        return new Builder();
    }

    private static class Builder implements
            RootStep,
            BuildStep,
            CreateStep,
            BotStep,
            DeleteStep,
            SettingStep,
            SystemStep,
            UseStep,
            PlayListStep
    {

        private final StringBuilder sb = new StringBuilder().append("/api");

        @Override
        public AudioBotCommandUri build() {
            return new AudioBotCommandUri(sb.toString());
        }

        @Override
        public SettingStep setting() {
            sb.append("/setting");
            return this;
        }

        @Override
        public BotStep bot() {
            sb.append("/bot");
            return this;
        }

        @Override
        public SystemStep system() {
            sb.append("/system");
            return this;
        }

        @Override
        public BuildStep name(String name) {
            sb.append("/").append(name);
            return this;
        }

        @Override
        public CreateStep create() {
            sb.append("/create");
            return this;
        }

        @Override
        public DeleteStep delete() {
            sb.append("/delete");
            return this;
        }

        @Override
        public BuildStep info() {
            sb.append("/info");
            return this;
        }

        @Override
        public BuildStep list() {
            sb.append("/list");
            return this;
        }

        @Override
        public UseStep use(Long id) {
            sb.append("/use/").append(id);
            return this;
        }

        @Override
        public BuildStep delete(String name) {
            sb.append("/delete/")
                    .append(name)
                    .append(")");
            return this;
        }

        @Override
        public PlayListStep playlist() {
            sb.append("/(/list");
            return this;
        }
    }
}

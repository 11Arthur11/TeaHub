package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.bot_group.BotStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.setting_group.SettingStep;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.step.system_group.SystemStep;

public interface RootStep {

    SettingStep setting();
    BotStep bot();
    SystemStep system();

}

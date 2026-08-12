package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._settings;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AudioBotSetSettingParameterBuilder {
    private final StringBuilder sb;

    public AudioBotSetSettingParameterBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotBuildStep connectAddress(String address) {
        sb.append("/connect.address/").append(address);
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep connectPassword(String password) {
        sb.append("/connect.server_password.pw/").append(password);
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep connectNickname(String nickname) {
        sb.append("/connect.name/").append(URLEncoder.encode(nickname, StandardCharsets.UTF_8));
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep connectOnRuntime(boolean connectOnRuntime) {
        sb.append("/run/").append(connectOnRuntime);
        return new AudioBotBuildStep(sb);
    }



}

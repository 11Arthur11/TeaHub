package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._use;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;

public class AudioBotPlayListCommandBuilder {

    private final StringBuilder sb;

    public AudioBotPlayListCommandBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotBuildStep list() {
        sb.append("/list)");
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep show(String fileName, int index, int size) {
        sb.append("/show/")
                .append(fileName)
                .append("/")
                .append(index)
                .append("/")
                .append(size)
                .append(")");
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep itemDelete(String fileName, int index, int size) {
        sb.append("/show/")
                .append(fileName)
                .append("/")
                .append(index)
                .append("/")
                .append(size)
                .append(")");
        return new AudioBotBuildStep(sb);
    }

}

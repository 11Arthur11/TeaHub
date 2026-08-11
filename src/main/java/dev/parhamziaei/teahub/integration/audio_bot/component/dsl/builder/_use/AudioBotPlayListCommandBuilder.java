package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._use;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.web.util.UriUtils;

public class AudioBotPlayListCommandBuilder {

    private final StringBuilder sb;

    public AudioBotPlayListCommandBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public AudioBotBuildStep delete(String filename) {
        sb.append("/delete/")
                .append(filename)
                .append(")");

        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep create(String fileName, String playlistName) {
        sb.append("/create/")
                .append(fileName)
                .append("/")
                .append(playlistName)
                .append(")");

        return new AudioBotBuildStep(sb);
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

    public AudioBotBuildStep itemDelete(String fileName, int index) {
        sb.append("/item/delete/")
                .append(fileName)
                .append("/")
                .append(index)
                .append(")");
        return new AudioBotBuildStep(sb);
    }

    public AudioBotBuildStep itemAdd(String fileName, String trackLink) {
        sb.append("/add/")
                .append(fileName)
                .append("/")
                .append(trackLink)
                .append(")");
        return new AudioBotBuildStep(sb);
    }


    public AudioBotBuildStep play(String fileName) {
        sb.append("/play/")
                .append(fileName)
                .append(")");

        return new AudioBotBuildStep(sb);
    }
}

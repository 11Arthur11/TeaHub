package dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder._use;

import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.builder.AudioBotBuildStep;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.web.util.UriUtils;

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

    public AudioBotBuildStep itemAdd(String fileName, String musicUrl) {
        sb.append("/add/")
                .append(fileName)
                .append("/")
                .append(UriUtils.encodePathSegment(musicUrl, "UTF-8"))
                .append(")");
        return new AudioBotBuildStep(sb);
    }



}

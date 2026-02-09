package dev.parhamziaei.teahub.exception.custom.service.audio_bot;

public class AudioBotNodeHasActiveInstanceException extends RuntimeException {
    public AudioBotNodeHasActiveInstanceException(String message) {
        super(message);
    }
    public AudioBotNodeHasActiveInstanceException() {
        super();
    }
}

package dev.parhamziaei.teahub.exception.custom.service.audio_bot;

public class AudioBotSynchronizationException extends RuntimeException {
    public AudioBotSynchronizationException(String message) {
        super(message);
    }
    public AudioBotSynchronizationException() {
        super();
    }
}

package dev.parhamziaei.teahub.exception.custom.service.audio_bot;

public class AudioBotAlreadyInitiatedException extends RuntimeException {
    public AudioBotAlreadyInitiatedException(String message) {
        super(message);
    }
}

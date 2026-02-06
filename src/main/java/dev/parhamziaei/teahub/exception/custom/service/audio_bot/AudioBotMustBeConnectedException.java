package dev.parhamziaei.teahub.exception.custom.service.audio_bot;

public class AudioBotMustBeConnectedException extends RuntimeException {
    public AudioBotMustBeConnectedException(String message) {
        super(message);
    }
    public AudioBotMustBeConnectedException() {
        super();
    }
}

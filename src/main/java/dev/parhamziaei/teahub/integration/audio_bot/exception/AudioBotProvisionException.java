package dev.parhamziaei.teahub.integration.audio_bot.exception;

public class AudioBotProvisionException extends RuntimeException {
    public AudioBotProvisionException(String message) {
        super(message);
    }
    public AudioBotProvisionException(){
        super();
    }
}

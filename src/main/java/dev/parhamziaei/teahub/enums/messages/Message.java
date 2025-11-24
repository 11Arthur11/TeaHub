package dev.parhamziaei.teahub.enums.messages;

public enum Message {

    //note: system errors
    SERVER_INTERNAL_ERROR("error.server.internal"),
    SERVER_IO_ERROR("error.server.io"),
    SERVER_VALIDATION_ERROR("error.server.validation"),
    SERVER_RESOURCE_NOT_FOUND("error.server.resource_not_found"),
    DEFAULT_FAILED("error.default_failed");

    //note: admin query instance managing service messages

    private final String key;

    Message(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}

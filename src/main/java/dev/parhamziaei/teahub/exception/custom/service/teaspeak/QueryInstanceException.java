package dev.parhamziaei.teahub.exception.custom.service.teaspeak;

public class QueryInstanceException extends RuntimeException {
    public QueryInstanceException(String message) {
        super(message);
    }
    public QueryInstanceException() {
        super();
    }
}

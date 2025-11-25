package dev.parhamziaei.teahub.exception.custom.service.teaspeak;

public class QueryInstanceNotFoundException extends RuntimeException {
    public QueryInstanceNotFoundException(String message) {
        super(message);
    }
    public QueryInstanceNotFoundException() {
        super();
    }
}

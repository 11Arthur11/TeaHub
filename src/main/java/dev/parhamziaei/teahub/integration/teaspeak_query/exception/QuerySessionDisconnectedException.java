package dev.parhamziaei.teahub.integration.teaspeak_query.exception;

public class QuerySessionDisconnectedException extends RuntimeException {
    public QuerySessionDisconnectedException(String message) {
        super(message);
    }
}

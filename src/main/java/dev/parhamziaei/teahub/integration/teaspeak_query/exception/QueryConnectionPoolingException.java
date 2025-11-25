package dev.parhamziaei.teahub.integration.teaspeak_query.exception;

public class QueryConnectionPoolingException extends RuntimeException {
    public QueryConnectionPoolingException(String message) {
        super(message);
    }
    public QueryConnectionPoolingException() {
        super();
    }
}

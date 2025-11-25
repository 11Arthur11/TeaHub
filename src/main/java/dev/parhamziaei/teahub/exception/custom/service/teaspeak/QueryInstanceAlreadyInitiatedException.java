package dev.parhamziaei.teahub.exception.custom.service.teaspeak;

public class QueryInstanceAlreadyInitiatedException extends QueryInstanceException {
    public QueryInstanceAlreadyInitiatedException(String message) {
        super(message);
    }

    public QueryInstanceAlreadyInitiatedException() {
        super();
    }
}

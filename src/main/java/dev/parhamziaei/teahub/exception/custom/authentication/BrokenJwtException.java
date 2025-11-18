package dev.parhamziaei.teahub.exception.custom.authentication;

public class BrokenJwtException extends RuntimeException {
    public BrokenJwtException(String message) {
        super(message);
    }
    public BrokenJwtException() {
      super();
    }
}

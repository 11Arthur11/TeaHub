package dev.parhamziaei.teahub.exception.custom.authorization;

public class NoSuchRoleException extends RuntimeException {
    public NoSuchRoleException(String message) {
        super(message);
    }
    public NoSuchRoleException() {
      super();
    }
}

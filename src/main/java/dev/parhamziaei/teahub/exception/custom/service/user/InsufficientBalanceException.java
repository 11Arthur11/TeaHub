package dev.parhamziaei.teahub.exception.custom.service.user;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
    public InsufficientBalanceException(){
        super();
    }
}

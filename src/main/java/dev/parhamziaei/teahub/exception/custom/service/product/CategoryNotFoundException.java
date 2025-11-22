package dev.parhamziaei.teahub.exception.custom.service.product;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
    public CategoryNotFoundException() {
        super();
    }
}

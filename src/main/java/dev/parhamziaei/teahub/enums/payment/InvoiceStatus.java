package dev.parhamziaei.teahub.enums.payment;

public enum InvoiceStatus {

    PAID("invoice.status.paid"),
    CANCELLED("invoice.status.cancelled"),
    PENDING("invoice.status.pending");

    private final String key;

    InvoiceStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}

package dev.parhamziaei.teahub.enums.payment;

public enum PostPaymentType {
    PROLONG("transaction-reason.prolong"),
    WALLET_CHARGE("transaction-reason.wallet-charge"),
    ADMIN_DEBT("transaction-reason.admin-debt"),;

    private final String key;
    PostPaymentType(String key) {
        this.key = key;
    }
    public String key() {
        return this.key;
    }


}

package dev.parhamziaei.teahub.enums.payment;

public enum TransactionReason {
    PROLONG("transaction-reason.prolong"),
    PURCHASE("transaction-reason.purchase"),
    REFUND("transaction-reason.refund"),
    WALLET_CHARGE("transaction-reason.wallet-charge");

    private final String key;
    TransactionReason(String key) {
        this.key = key;
    }
    public String key() {
        return this.key;
    }


}

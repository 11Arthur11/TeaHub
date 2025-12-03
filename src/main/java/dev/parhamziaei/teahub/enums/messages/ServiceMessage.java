package dev.parhamziaei.teahub.enums.messages;

public enum ServiceMessage {

    //note: ticket service messages
    TICKET_MAX_ATTACHMENT_REACHED("error.service.ticket.max_attachment_reached"),
    TICKET_MESSAGE_SENT("success.service.ticket.message_sent"),
    TICKET_SUBMITTED("success.service.ticket.submitted"),
    TICKET_EDITED("success.service.ticket.edited"),

    //note: file storage service errors
    PAYLOAD_TOO_LARGE("error.service.file.payload-too-large"),
    MEDIA_TYPE_NOT_ALLOWED("error.service.file.type-not-allowed"),
    FILE_STORAGE_ERROR("error.service.file.storage"),

    USER_WALLET_CHARGE_AMOUNT_TOO_SMALL("error.service.wallet.charge-amount-too-small"),

    SHOP_CATEGORY_NOT_FOUND("error.service.shop.category-not-found"),

    PAYMENT_ERROR("error.service.payment"),
    PAYMENT_FAILED("error.service.payment-failed"),
    PAYMENT_INVOICE_ERROR("error.service.invoice"),
    PAYMENT_INVOICE_CREATED("success.service.invoice.created"),
    PAYMENT_INVOICE_PAID("success.service.invoice.paid"),

    PAYMENT_GATEWAY_CREATED("success.service.gateway.created"),
    PAYMENT_GATEWAY_ERROR("error.service.gateway"),
    PAYMENT_GATEWAY_CONFIG_ERROR("error.service.gateway.config"),
    PAYMENT_GATEWAY_NOT_FOUND("error.service.gateway.not-found"),

    QUERY_INSTANCE_INITIATED("success.service.query-instance.initiated"),
    QUERY_INSTANCE_ALREADY_EXIST("error.service.query-instance.already-exists"),
    QUERY_INSTANCE_ENABLING("success.service.query-instance.enabling"),
    QUERY_INSTANCE_DISABLING("success.service.query-instance.disabling"),
    QUERY_INSTANCE_DELETED("success.service.query-instance.deleted"),
    QUERY_INSTANCE_PORT_RANGE_INVALID("error.service.query-instance.port-range-invalid"),
    QUERY_INSTANCE_NOT_FOUND("error.service.query-instance.not-found"),
    QUERY_INSTANCE_ERROR("error.service.query-instance"),;

    private final String key;

    ServiceMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

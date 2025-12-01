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

    PAYMENT_INVOICE_CREATED("success.service.invoice.created"),
    PAYMENT_INVOICE_PAID("success.service.invoice.paid"),

    QUERY_INSTANCE_INITIATED("success.service.query-instance.initiated"),
    QUERY_INSTANCE_ALREADY_EXIST("error.service.query-instance.already-exists"),
    QUERY_INSTANCE_ENABLING("success.service.query-instance.enabling"),
    QUERY_INSTANCE_DISABLING("success.service.query-instance.disabling"),
    QUERY_INSTANCE_DELETED("success.service.query-instance.deleted"),;

    private final String key;

    ServiceMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

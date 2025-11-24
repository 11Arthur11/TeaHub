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

    QUERY_INSTANCE_INITIATED("success.service.query-instance.initiated");

    private final String key;

    ServiceMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

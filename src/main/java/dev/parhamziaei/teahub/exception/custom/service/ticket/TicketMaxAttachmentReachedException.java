package dev.parhamziaei.teahub.exception.custom.service.ticket;

public class TicketMaxAttachmentReachedException extends TicketServiceException {
    public TicketMaxAttachmentReachedException(String message) {
        super(message);
    }
}

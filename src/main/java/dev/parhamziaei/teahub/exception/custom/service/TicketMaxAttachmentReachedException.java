package dev.parhamziaei.teahub.exception.custom.service;

public class TicketMaxAttachmentReachedException extends TicketServiceException {
    public TicketMaxAttachmentReachedException(String message) {
        super(message);
    }
}

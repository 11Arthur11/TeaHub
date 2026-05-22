package dev.parhamziaei.teahub.exception.custom.service.dns;

public class DnsProviderNotConfiguredException extends RuntimeException {
    public DnsProviderNotConfiguredException(String message) {
        super(message);
    }
    public DnsProviderNotConfiguredException() {
        super();
    }
}

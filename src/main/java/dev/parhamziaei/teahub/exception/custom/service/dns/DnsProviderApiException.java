package dev.parhamziaei.teahub.exception.custom.service.dns;

import dev.parhamziaei.teahub.enums.dns.DnsProviderType;

public class DnsProviderApiException extends RuntimeException {
  public DnsProviderApiException(String message) {
    super(message);
  }
  public DnsProviderApiException() {
    super();
  }
}

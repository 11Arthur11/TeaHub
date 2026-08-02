package dev.parhamziaei.teahub.integration.zone_manager.component;

import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

@Service
public class DnsProviderRegistry {

    private final EnumMap<DnsProviderType, DnsProviderGateway> providers = new EnumMap<>(DnsProviderType.class);

    public DnsProviderRegistry(
            List<DnsProviderGateway> providers
    ) {
        providers.forEach(provider -> this.providers.put(provider.getType(), provider));
    }

    public DnsProviderGateway getProvider(DnsProviderType providerType) {
        return providers.get(providerType);
    }

    public void executeToAll(Consumer<? super DnsProviderGateway> consumer) {
        this.providers.values()
                .forEach(consumer);
    }

}

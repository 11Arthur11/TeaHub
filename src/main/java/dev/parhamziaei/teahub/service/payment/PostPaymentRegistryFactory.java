package dev.parhamziaei.teahub.service.payment;

import dev.parhamziaei.teahub.enums.payment.PostPaymentType;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.service.payment.registry.PostPaymentRegistryHandler;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostPaymentRegistryFactory {

    private final EnumMap<PostPaymentType, PostPaymentRegistryHandler> registries = new EnumMap<>(PostPaymentType.class);

    public PostPaymentRegistryFactory(List<PostPaymentRegistryHandler> handlers) {
        handlers.forEach(h -> registries.put(h.getType(), h));
    }

    public PostPaymentRegistryHandler getHandler(PostPaymentType type) {
        return registries.get(type);
    }

}

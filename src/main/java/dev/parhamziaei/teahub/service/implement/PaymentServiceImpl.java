package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.entity.jpa.user.Invoice;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.service.InvoiceService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public String createInvoice(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Invoice invoice = new Invoice(user, new Money(amount));
        invoiceRepository.save(invoice);
        return invoice.getInvoiceToken();
    }
}

package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.dto.request.payment.admin.AdminDebtInvoiceRequest;
import dev.parhamziaei.teahub.dto.request.query.InvoiceAdminFilterRequest;
import dev.parhamziaei.teahub.dto.request.query.InvoiceFilterRequest;
import dev.parhamziaei.teahub.dto.response.payment.admin.InvoiceAdminResponse;
import dev.parhamziaei.teahub.dto.response.payment.admin.PaymentTransactionDetailResponse;
import dev.parhamziaei.teahub.dto.response.payment.user.InvoiceUserResponse;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.AdminDebtPostPayment;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.ProlongPostPayment;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.WalletChargePostPayment;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.messages.Text;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.WalletChargeAmountTooSmallException;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final PaymentServiceProperties paymentProperties;
    private final UserRepository userRepository;

    public InvoiceUserResponse getOwnInvoice(Long userId, String invoiceToken) {
        Specification<Invoice> spec = Specification.allOf(
                InvoiceSpecification.hasInvoiceToken(invoiceToken)
                        .and(InvoiceSpecification.mustHaveOwnerId(userId))
        );

        Invoice invoice = invoiceRepo.findOne(spec)
                .orElseThrow(NoSuchEntityException::new);

        return modelMapper.map(invoice, InvoiceUserResponse.class);
    }

    public String createChargeWalletInvoice(Long userId, BigDecimal amount) {
        if (amount.compareTo(paymentProperties.minimumWalletChargeAmountIrt()) < 0)
            throw new WalletChargeAmountTooSmallException(paymentProperties.minimumWalletChargeAmountIrt());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Invoice invoice = new Invoice(user, new Money(amount));
        invoice.setPostPaymentAction(new WalletChargePostPayment());
        invoice.setDescription(messageService.get(Text.INVOICE_REASON_CREDIT));
        invoiceRepo.save(invoice);
        return invoice.getInvoiceToken();
    }

    public String createAdminDebtInvoice(Long adminId, AdminDebtInvoiceRequest request) {
        User user = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Invoice invoice = new Invoice(user, request.getAmount());
        invoice.setPostPaymentAction(new AdminDebtPostPayment(adminId));
        invoice.setDescription(request.getDescription());
        invoiceRepo.save(invoice);
        return invoice.getInvoiceToken();
    }

    public String createProlongInvoice(Long userId, BigDecimal amount, Long resourceId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Invoice invoice = new Invoice(user, new Money(amount));
        invoice.setPostPaymentAction(new ProlongPostPayment(resourceId));
        invoice.setDescription(messageService.get(Text.INVOICE_REASON_PROLONG) + String.format("%06d", resourceId));
        invoiceRepo.save(invoice);
        return invoice.getInvoiceToken();
    }

    public PagedModel<InvoiceAdminResponse> getAllInvoices(InvoiceAdminFilterRequest filterRequest) {
        Specification<Invoice> spec = Specification.allOf(
                InvoiceSpecification.hasStatus(filterRequest.getStatus())
                        .and(InvoiceSpecification.hasUserId(filterRequest.getByUserId()))
                        .and(InvoiceSpecification.betweenTime(filterRequest.getFromCreatedAt(), filterRequest.getToCreatedAt()))
        );

        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        Page<Invoice> invoices = invoiceRepo.findAll(spec, pageable);

        List<InvoiceAdminResponse> response = invoices.stream()
                .map(i -> {
                    InvoiceAdminResponse r = modelMapper.map(i, InvoiceAdminResponse.class);
                    if (i.getPaymentTransaction() != null)
                        r.setPaymentTransaction(modelMapper.map(i.getPaymentTransaction(), PaymentTransactionDetailResponse.class));
                    return r;
                })
                .sorted(Comparator.comparing(InvoiceAdminResponse::getCreatedAt))
                .toList();

        if (response.isEmpty())
            throw new NoSuchDataException();

        return new PagedModel<>(new PageImpl<>(response, pageable, invoices.getTotalElements()));
    }

    public PagedModel<InvoiceUserResponse> getByUserId(InvoiceFilterRequest filterRequest, Long userId) {
        Specification<Invoice> spec = Specification.allOf(
                InvoiceSpecification.hasStatus(filterRequest.getStatus())
                        .and(InvoiceSpecification.mustHaveOwnerId(userId))
                        .and(InvoiceSpecification.betweenTime(filterRequest.getFromCreatedAt(), filterRequest.getToCreatedAt()))
        );

        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        Page<Invoice> invoices = invoiceRepo.findAll(spec, pageable);

        List<InvoiceUserResponse> response = invoiceRepo.findAll(spec, PageRequest.of(filterRequest.getPage(), filterRequest.getSize()))
                .stream()
                .map(i -> modelMapper.map(i, InvoiceUserResponse.class))
                .sorted(Comparator.comparing(InvoiceUserResponse::getCreatedAt))
                .toList();

        if (response.isEmpty())
            throw new NoSuchDataException();

        return new PagedModel<>(new PageImpl<>(response, pageable, invoices.getTotalElements()));
    }

    public void changeStatus(String invoiceToken, InvoiceStatus invoiceStatus) {
        Invoice invoice = invoiceRepo.findOne(InvoiceSpecification.hasInvoiceToken(invoiceToken))
                .orElseThrow(NoSuchEntityException::new);
        invoice.setStatus(invoiceStatus);
        invoiceRepo.save(invoice);
    }

}

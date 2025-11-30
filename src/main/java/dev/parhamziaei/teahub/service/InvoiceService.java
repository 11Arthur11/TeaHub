package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.response.shop.user.InvoiceUserResponse;
import dev.parhamziaei.teahub.entity.jpa.user.Invoice;
import dev.parhamziaei.teahub.enums.InvoiceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    private final MessageService messageService;

    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public InvoiceUserResponse getOwnInvoice(Long userId, String invoiceToken) {
        Specification<Invoice> spec = Specification.allOf(
                InvoiceSpecification.hasInvoiceToken(invoiceToken)
                        .and(InvoiceSpecification.mustHaveOwnerId(userId))
        );

        Invoice invoice = invoiceRepository.findOne(spec)
                .orElseThrow(NoSuchEntityException::new);

        InvoiceUserResponse response = modelMapper.map(invoice, InvoiceUserResponse.class);
        response.setStatus(messageService.get(invoice.getStatus()));
        return response;
    }

    public PagedModel<Invoice> getByUserId(Pageable pageable, Long userId, InvoiceStatus invoiceStatus) {
        Specification<Invoice> spec = Specification.allOf(
                InvoiceSpecification.hasStatus(invoiceStatus)
                        .and(InvoiceSpecification.hasUserId(userId))
        );

        return new PagedModel<>(invoiceRepository.findAll(spec, pageable));
    }

    public void changeStatus(String invoiceToken, InvoiceStatus invoiceStatus) {
        Invoice invoice = invoiceRepository.findOne(InvoiceSpecification.hasInvoiceToken(invoiceToken))
                .orElseThrow(NoSuchEntityException::new);
        invoice.setStatus(invoiceStatus);
        invoiceRepository.save(invoice);
    }

}

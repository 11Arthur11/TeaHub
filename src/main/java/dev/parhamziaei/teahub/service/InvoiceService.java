package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.query.InvoiceAdminFilterRequest;
import dev.parhamziaei.teahub.dto.request.query.InvoiceFilterRequest;
import dev.parhamziaei.teahub.dto.response.payment.admin.InvoiceAdminResponse;
import dev.parhamziaei.teahub.dto.response.payment.admin.PaymentTransactionDetailResponse;
import dev.parhamziaei.teahub.dto.response.payment.user.InvoiceUserResponse;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.entity.jpa.payment.PaymentTransaction;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    private final MessageService messageService;

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

    public PagedModel<InvoiceAdminResponse> getAllInvoices(InvoiceAdminFilterRequest filterRequest) {
        Specification<Invoice> spec = Specification.allOf(
                InvoiceSpecification.hasStatus(filterRequest.getStatus())
                        .and(InvoiceSpecification.hasUserId(filterRequest.getByUserId()))
                        .and(InvoiceSpecification.betweenTime(filterRequest.getFromCreatedAt(), filterRequest.getToCreatedAt()))
        );

        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        Page<Invoice> invoices = invoiceRepository.findAll(spec, pageable);

        List<InvoiceAdminResponse> response = invoices.stream()
                .map(i -> {
                    InvoiceAdminResponse r = modelMapper.map(i, InvoiceAdminResponse.class);
                    r.setStatus(messageService.get(i.getStatus()));
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
        Page<Invoice> invoices = invoiceRepository.findAll(spec, pageable);

        List<InvoiceUserResponse> response = invoiceRepository.findAll(spec, PageRequest.of(filterRequest.getPage(), filterRequest.getSize()))
                .stream()
                .map(i -> {
                    InvoiceUserResponse r = modelMapper.map(i, InvoiceUserResponse.class);
                    r.setStatus(messageService.get(i.getStatus()));
                    return r;
                })
                .sorted(Comparator.comparing(InvoiceUserResponse::getCreatedAt))
                .toList();

        if (response.isEmpty())
            throw new NoSuchDataException();

        return new PagedModel<>(new PageImpl<>(response, pageable, invoices.getTotalElements()));
    }

    public void changeStatus(String invoiceToken, InvoiceStatus invoiceStatus) {
        Invoice invoice = invoiceRepository.findOne(InvoiceSpecification.hasInvoiceToken(invoiceToken))
                .orElseThrow(NoSuchEntityException::new);
        invoice.setStatus(invoiceStatus);
        invoiceRepository.save(invoice);
    }

}

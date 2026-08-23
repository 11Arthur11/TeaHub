package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.entity.jpa.payment.AqayePardakhtGateway;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.PaymentTransaction;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.PostPaymentAction;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.enums.payment.PostPaymentType;
import dev.parhamziaei.teahub.exception.custom.service.payment.InvoiceException;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayFactory;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.*;
import dev.parhamziaei.teahub.service.implement.PaymentServiceImpl;
import dev.parhamziaei.teahub.service.payment.PostPaymentRegistryFactory;
import dev.parhamziaei.teahub.service.payment.registry.PostPaymentRegistryHandler;
import dev.parhamziaei.teahub.support.TestFixtures;
import dev.parhamziaei.teahub.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private InvoiceRepository invoiceRepository;
    @Mock private PaymentGatewayFactory gatewayFactory;
    @Mock private GatewayRepository gatewayRepository;
    @Mock private PaymentTransactionRepository transactionRepository;
    @Mock private WalletService walletService;
    @Mock private AqayePardakhtGatewayRepository aqayePardakhtRepository;
    @Mock private PostPaymentRegistryFactory postPaymentFactory;
    @Mock private PaymentGatewayHandler gatewayHandler;
    @Mock private PostPaymentRegistryHandler postPaymentHandler;

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(
                invoiceRepository,
                gatewayFactory,
                gatewayRepository,
                transactionRepository,
                walletService,
                aqayePardakhtRepository,
                postPaymentFactory
        );
    }

    @Test
    void createsGatewayUriOnlyForPendingOwnedInvoice() {
        Gateway gateway = new Gateway(PaymentGatewayType.AQAYE_PARDAKHT, 1L);
        Invoice invoice = invoice(InvoiceStatus.PENDING);
        when(gatewayRepository.findById(1L)).thenReturn(Optional.of(gateway));
        when(invoiceRepository.findOne(ArgumentMatchers.<Specification<Invoice>>any()))
                .thenReturn(Optional.of(invoice));
        when(gatewayFactory.getGateway(PaymentGatewayType.AQAYE_PARDAKHT)).thenReturn(gatewayHandler);
        when(gatewayHandler.createPaymentGateway(invoice)).thenReturn("https://gateway.test/pay/1");

        String uri = paymentService.createPaymentGatewayUri(1L, "INVOICE_1", 1L);

        assertEquals("https://gateway.test/pay/1", uri);
    }

    @Test
    void paidInvoiceCannotCreateAnotherGatewayTransaction() {
        Gateway gateway = new Gateway(PaymentGatewayType.AQAYE_PARDAKHT, 1L);
        Invoice invoice = invoice(InvoiceStatus.PAID);
        when(gatewayRepository.findById(1L)).thenReturn(Optional.of(gateway));
        when(invoiceRepository.findOne(ArgumentMatchers.<Specification<Invoice>>any()))
                .thenReturn(Optional.of(invoice));

        assertThrows(
                InvoiceException.class,
                () -> paymentService.createPaymentGatewayUri(1L, "INVOICE_1", 1L)
        );
        verifyNoInteractions(gatewayFactory);
    }

    @Test
    void verifiedCallbackProcessesActionMarksInvoicePaidAndStoresTransaction() {
        Invoice invoice = invoice(InvoiceStatus.PENDING);
        PostPaymentAction action = mock(PostPaymentAction.class);
        when(action.getPostPaymentType()).thenReturn(PostPaymentType.WALLET_CHARGE);
        invoice.setPostPaymentAction(action);
        APCallbackRequest callback = callback();
        AqayePardakhtGateway gateway = new AqayePardakhtGateway();
        gateway.setName("Aqaye Pardakht");
        when(gatewayFactory.getGateway(PaymentGatewayType.AQAYE_PARDAKHT)).thenReturn(gatewayHandler);
        when(invoiceRepository.findOne(ArgumentMatchers.<Specification<Invoice>>any()))
                .thenReturn(Optional.of(invoice));
        when(postPaymentFactory.getHandler(PostPaymentType.WALLET_CHARGE)).thenReturn(postPaymentHandler);
        when(aqayePardakhtRepository.find()).thenReturn(Optional.of(gateway));

        paymentService.verifyAPCallback(callback);

        verify(gatewayHandler).verifyTransaction(callback);
        verify(postPaymentHandler).processAction(invoice);
        assertEquals(InvoiceStatus.PAID, invoice.getStatus());
        assertNotNull(invoice.getPaidAt());
        verify(invoiceRepository).save(invoice);
        ArgumentCaptor<PaymentTransaction> transaction = ArgumentCaptor.forClass(PaymentTransaction.class);
        verify(transactionRepository).save(transaction.capture());
        assertSame(transaction.getValue(), invoice.getPaymentTransaction());
    }

    @Test
    void duplicateCallbackCannotRunPostPaymentActionTwice() {
        Invoice invoice = invoice(InvoiceStatus.PAID);
        APCallbackRequest callback = callback();
        when(gatewayFactory.getGateway(PaymentGatewayType.AQAYE_PARDAKHT)).thenReturn(gatewayHandler);
        when(invoiceRepository.findOne(ArgumentMatchers.<Specification<Invoice>>any()))
                .thenReturn(Optional.of(invoice));

        assertThrows(InvoiceException.class, () -> paymentService.verifyAPCallback(callback));

        verifyNoInteractions(postPaymentFactory, postPaymentHandler, transactionRepository);
        verify(invoiceRepository, never()).save(any());
    }

    private Invoice invoice(InvoiceStatus status) {
        Invoice invoice = new Invoice(TestFixtures.user(1L, 10L, BigDecimal.ZERO), new Money(new BigDecimal("10000")));
        invoice.setId(22L);
        invoice.setInvoiceToken("INVOICE_1");
        invoice.setTaxPercentage(0);
        invoice.setStatus(status);
        return invoice;
    }

    private APCallbackRequest callback() {
        APCallbackRequest callback = new APCallbackRequest();
        callback.setStatus("1");
        callback.setTransid("transaction-1");
        callback.setInvoiceId("INVOICE_1");
        callback.setTracking_number("tracking-1");
        return callback;
    }
}

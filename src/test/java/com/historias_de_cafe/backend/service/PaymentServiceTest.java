package com.historias_de_cafe.backend.service;

import com.historias_de_cafe.backend.DTO.PaymentRequestDto;
import com.historias_de_cafe.backend.DTO.PaymentResponseDto;
import com.historias_de_cafe.backend.model.Order;
import com.historias_de_cafe.backend.model.Payment;
import com.historias_de_cafe.backend.model.Role;
import com.historias_de_cafe.backend.model.User;
import com.historias_de_cafe.backend.repository.OrderRepository;
import com.historias_de_cafe.backend.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void createPaymentPreferenceThrowsWhenAccessTokenIsMissing() {
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, " ");

        assertThrows(RuntimeException.class, () -> paymentService.createPaymentPreference(new PaymentRequestDto(1L)));
        verify(orderRepository, never()).findById(1L);
    }

    @Test
    void createPaymentPreferenceThrowsWhenOrderDoesNotExist() {
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, "access-token");
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.createPaymentPreference(new PaymentRequestDto(99L)));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void createPaymentPreferenceThrowsWhenPaymentAlreadyExistsForOrder() {
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, "access-token");
        Order order = order();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(payment()));

        assertThrows(RuntimeException.class, () -> paymentService.createPaymentPreference(new PaymentRequestDto(1L)));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void getByIdReturnsPaymentDto() {
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, "access-token");
        when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment()));

        PaymentResponseDto response = paymentService.getById(5L);

        assertEquals(5L, response.id());
        assertEquals(1L, response.orderId());
        assertEquals("tx-1", response.transactionNumber());
        assertEquals("En proceso", response.transactionStatus());
    }

    @Test
    void getByOrderIdThrowsWhenPaymentDoesNotExist() {
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, "access-token");
        when(paymentRepository.findByOrderId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.getByOrderId(99L));
    }

    @Test
    void updateStatusApprovesPaymentAndMovesOrderToPendingDelivery() {
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, "access-token");
        Payment payment = payment();

        when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        PaymentResponseDto response = paymentService.updateStatus(5L, "APPROVED", "tx-approved");

        assertEquals("Aprobado", response.transactionStatus());
        assertEquals("tx-approved", response.transactionNumber());
        assertEquals("Pendiente Entrega", payment.getOrder().getStateOrder());
        assertNotNull(payment.getTransactionDate());
    }

    @Test
    void updateStatusCancelsRejectedPaymentAndOrder() {
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, "access-token");
        Payment payment = payment();

        when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        PaymentResponseDto response = paymentService.updateStatus(5L, "REJECTED", null);

        assertEquals("Cancelado", response.transactionStatus());
        assertEquals("Cancelado", payment.getOrder().getStateOrder());
    }

    private Payment payment() {
        Payment payment = new Payment();
        payment.setId(5L);
        payment.setOrder(order());
        payment.setTransactionNumber("tx-1");
        payment.setTransactionDate(LocalDateTime.now());
        payment.setTransactionStatus("En proceso");
        return payment;
    }

    private Order order() {
        User user = new User(1L, "Ana Perez", "ana@example.com", "password123", Role.CLIENT, LocalDateTime.now(), true);
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStateOrder("En proceso");
        order.setSubtotal(BigDecimal.valueOf(35000.0));
        order.setTotal(BigDecimal.valueOf(35000.0));
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
}

package br.com.moraesit.order.service.application.ports.input.message.listener.payment;

import br.com.moraesit.order.service.application.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}

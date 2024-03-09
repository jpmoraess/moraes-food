package br.com.moraesit.payment.service.application.ports.input.message.listener;

import br.com.moraesit.payment.service.application.dto.PaymentRequest;

public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequest paymentRequest);

    void cancelPayment(PaymentRequest paymentRequest);
}

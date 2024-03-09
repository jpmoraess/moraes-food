package br.com.moraesit.payment.service.application;

import br.com.moraesit.payment.service.application.dto.PaymentRequest;
import br.com.moraesit.payment.service.application.ports.input.message.listener.PaymentRequestMessageListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    public PaymentRequestMessageListenerImpl(PaymentRequestHelper paymentRequestHelper) {
        this.paymentRequestHelper = paymentRequestHelper;
    }

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistCancelPayment(paymentRequest);
    }
}

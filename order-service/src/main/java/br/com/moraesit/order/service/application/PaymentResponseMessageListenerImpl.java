package br.com.moraesit.order.service.application;

import br.com.moraesit.order.service.application.dto.message.PaymentResponse;
import br.com.moraesit.order.service.application.ports.input.message.listener.payment.PaymentResponseMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final Logger logger = LoggerFactory.getLogger(PaymentResponseMessageListenerImpl.class);

    private final OrderPaymentSaga orderPaymentSaga;

    public PaymentResponseMessageListenerImpl(OrderPaymentSaga orderPaymentSaga) {
        this.orderPaymentSaga = orderPaymentSaga;
    }

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        orderPaymentSaga.process(paymentResponse);
        logger.info("Order Payment Saga process operation is completed for order id: {}", paymentResponse.getOrderId());
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        logger.info("Order is roll backed for order id: {} with failure messages: {}",
                paymentResponse.getOrderId(), String.join(",", paymentResponse.getFailureMessages()));
    }
}

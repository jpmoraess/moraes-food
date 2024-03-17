package br.com.moraesit.order.service.application;

import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderSagaHelper {

    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case PAID -> {
                return SagaStatus.PROCESSING;
            }
            case APPROVED -> {
                return SagaStatus.SUCCEEDED;
            }
            case CANCELLING -> {
                return SagaStatus.COMPENSATING;
            }
            case CANCELLED -> {
                return SagaStatus.COMPENSATED;
            }
            default -> {
                return SagaStatus.STARTED;
            }
        }
    }
}

package br.com.moraesit.payment.service.application;

import br.com.moraesit.payment.service.domain.entity.CreditEntry;
import br.com.moraesit.payment.service.domain.entity.CreditHistory;
import br.com.moraesit.payment.service.domain.entity.Payment;
import br.com.moraesit.payment.service.domain.event.PaymentEvent;

import java.util.List;

@FunctionalInterface
public interface PaymentOperation {

    PaymentEvent execute(Payment payment, CreditEntry creditEntry,
                         List<CreditHistory> creditHistoryList, List<String> failureMessages);
}

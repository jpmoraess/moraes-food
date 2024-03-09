package br.com.moraesit.payment.service.domain;

import br.com.moraesit.payment.service.domain.entity.CreditEntry;
import br.com.moraesit.payment.service.domain.entity.CreditHistory;
import br.com.moraesit.payment.service.domain.entity.Payment;
import br.com.moraesit.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                            List<CreditHistory> creditHistoryList, List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                          List<CreditHistory> creditHistoryList, List<String> failureMessages);
}

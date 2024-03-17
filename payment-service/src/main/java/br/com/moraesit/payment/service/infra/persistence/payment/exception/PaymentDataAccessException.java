package br.com.moraesit.payment.service.infra.persistence.payment.exception;

public class PaymentDataAccessException extends RuntimeException {

    public PaymentDataAccessException(String message) {
        super(message);
    }
}

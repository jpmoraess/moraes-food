package br.com.moraesit.payment.service.infra.persistence.creditentry.exception;

public class CreditEntryDataAccessException extends RuntimeException {

    public CreditEntryDataAccessException(String message) {
        super(message);
    }
}

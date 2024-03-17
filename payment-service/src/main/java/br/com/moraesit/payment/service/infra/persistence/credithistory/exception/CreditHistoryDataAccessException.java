package br.com.moraesit.payment.service.infra.persistence.credithistory.exception;

public class CreditHistoryDataAccessException extends RuntimeException {

    public CreditHistoryDataAccessException(String message) {
        super(message);
    }
}

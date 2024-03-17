package br.com.moraesit.commons.saga;

public interface SagaStep<T> {

    void process(T data);

    void rollback(T data);
}

package br.com.moraesit.order.service.infra.persistence.restaurant.exception;

public class RestaurantDataAccessException extends RuntimeException {

    public RestaurantDataAccessException(String message) {
        super(message);
    }
}

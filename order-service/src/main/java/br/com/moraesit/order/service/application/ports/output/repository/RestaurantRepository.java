package br.com.moraesit.order.service.application.ports.output.repository;

import br.com.moraesit.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}

package br.com.moraesit.restaurant.service.application.ports.output.repository;

import br.com.moraesit.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}

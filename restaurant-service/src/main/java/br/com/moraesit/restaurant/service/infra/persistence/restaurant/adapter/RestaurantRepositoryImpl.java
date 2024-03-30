package br.com.moraesit.restaurant.service.infra.persistence.restaurant.adapter;

import br.com.moraesit.commons.persistence.entity.RestaurantEntity;
import br.com.moraesit.commons.persistence.repository.RestaurantJpaRepository;
import br.com.moraesit.restaurant.service.application.ports.output.repository.RestaurantRepository;
import br.com.moraesit.restaurant.service.domain.entity.Restaurant;
import br.com.moraesit.restaurant.service.infra.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts = RestaurantPersistenceMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);
        return restaurantEntities.map(RestaurantPersistenceMapper::restaurantEntityToRestaurant);
    }
}

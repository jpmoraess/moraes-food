package br.com.moraesit.order.service.infra.persistence.restaurant.adapter;

import br.com.moraesit.order.service.application.ports.output.repository.RestaurantRepository;
import br.com.moraesit.order.service.domain.entity.Restaurant;
import br.com.moraesit.order.service.infra.persistence.restaurant.entity.RestaurantEntity;
import br.com.moraesit.order.service.infra.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import br.com.moraesit.order.service.infra.persistence.restaurant.repository.RestaurantJpaRepository;
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

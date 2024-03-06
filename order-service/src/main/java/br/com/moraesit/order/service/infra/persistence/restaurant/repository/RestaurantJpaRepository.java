package br.com.moraesit.order.service.infra.persistence.restaurant.repository;

import br.com.moraesit.order.service.infra.persistence.restaurant.entity.RestaurantEntity;
import br.com.moraesit.order.service.infra.persistence.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
}

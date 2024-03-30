package br.com.moraesit.order.service.infra.persistence.restaurant.mapper;

import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.ProductId;
import br.com.moraesit.commons.domain.valueobject.RestaurantId;
import br.com.moraesit.commons.persistence.entity.RestaurantEntity;
import br.com.moraesit.commons.persistence.exception.RestaurantDataAccessException;
import br.com.moraesit.order.service.domain.entity.Product;
import br.com.moraesit.order.service.domain.entity.Restaurant;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantPersistenceMapper {

    public static List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public static Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity = restaurantEntities.stream()
                .findFirst()
                .orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity -> new Product(new ProductId(entity.getProductId()), entity.getProductName(), new Money(entity.getProductPrice())))
                .collect(Collectors.toList());

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}

package br.com.moraesit.restaurant.service.infra.persistence.restaurant.mapper;

import br.com.moraesit.commons.domain.valueobject.Money;
import br.com.moraesit.commons.domain.valueobject.ProductId;
import br.com.moraesit.commons.domain.valueobject.RestaurantId;
import br.com.moraesit.commons.persistence.entity.RestaurantEntity;
import br.com.moraesit.commons.persistence.exception.RestaurantDataAccessException;
import br.com.moraesit.restaurant.service.domain.entity.OrderDetail;
import br.com.moraesit.restaurant.service.domain.entity.Product;
import br.com.moraesit.restaurant.service.domain.entity.Restaurant;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantPersistenceMapper {

    public static List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public static Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("No restaurants found!"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                        Product.builder()
                                .productId(new ProductId(entity.getProductId()))
                                .name(entity.getProductName())
                                .price(new Money(entity.getProductPrice()))
                                .available(entity.getProductAvailable())
                                .build())
                .collect(Collectors.toList());

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .orderDetail(OrderDetail.builder()
                        .products(restaurantProducts)
                        .build())
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}

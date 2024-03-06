package br.com.moraesit.order.service.infra.persistence.restaurant.entity;

import java.io.Serializable;
import java.util.UUID;

public class RestaurantEntityId implements Serializable {

    private UUID restaurantId;
    private UUID productId;

    public RestaurantEntityId() {
    }

    public RestaurantEntityId(UUID restaurantId, UUID productId) {
        this.restaurantId = restaurantId;
        this.productId = productId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public UUID getProductId() {
        return productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantEntityId that = (RestaurantEntityId) o;

        if (!restaurantId.equals(that.restaurantId)) return false;
        return productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = restaurantId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}

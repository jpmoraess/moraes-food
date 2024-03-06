package br.com.moraesit.order.service.infra.persistence.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@IdClass(RestaurantEntityId.class)
@Table(name = "order_restaurant_m_view", schema = "restaurant")
public class RestaurantEntity {

    @Id
    private UUID restaurantId;
    @Id
    private UUID productId;
    private String restaurantName;
    private Boolean restaurantActive;
    private String productName;
    private BigDecimal productPrice;

    public RestaurantEntity() {
    }

    public RestaurantEntity(UUID restaurantId, UUID productId, String restaurantName, Boolean restaurantActive, String productName, BigDecimal productPrice) {
        this.restaurantId = restaurantId;
        this.productId = productId;
        this.restaurantName = restaurantName;
        this.restaurantActive = restaurantActive;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Boolean getRestaurantActive() {
        return restaurantActive;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantEntity that = (RestaurantEntity) o;

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

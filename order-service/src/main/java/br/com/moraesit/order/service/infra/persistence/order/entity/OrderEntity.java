package br.com.moraesit.order.service.infra.persistence.order.entity;

import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private UUID restaurantId;
    private UUID trackingId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String failureMessages;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddressEntity address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;

    public OrderEntity() {
    }

    public OrderEntity(UUID id, UUID customerId, UUID restaurantId, UUID trackingId, BigDecimal price,
                       OrderStatus orderStatus, String failureMessages, OrderAddressEntity address, List<OrderItemEntity> items) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.trackingId = trackingId;
        this.price = price;
        this.orderStatus = orderStatus;
        this.failureMessages = failureMessages;
        this.address = address;
        this.items = items;
    }

    private OrderEntity(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        trackingId = builder.trackingId;
        price = builder.price;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
        address = builder.address;
        items = builder.items;
    }

    public static Builder builder() {
        return new Builder();
    }


    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public UUID getTrackingId() {
        return trackingId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getFailureMessages() {
        return failureMessages;
    }

    public OrderAddressEntity getAddress() {
        return address;
    }

    public List<OrderItemEntity> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntity that = (OrderEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final class Builder {
        private UUID id;
        private UUID customerId;
        private UUID restaurantId;
        private UUID trackingId;
        private BigDecimal price;
        private OrderStatus orderStatus;
        private String failureMessages;
        private OrderAddressEntity address;
        private List<OrderItemEntity> items;

        private Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder customerId(UUID val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(UUID val) {
            restaurantId = val;
            return this;
        }

        public Builder trackingId(UUID val) {
            trackingId = val;
            return this;
        }

        public Builder price(BigDecimal val) {
            price = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(String val) {
            failureMessages = val;
            return this;
        }

        public Builder address(OrderAddressEntity val) {
            address = val;
            return this;
        }

        public Builder items(List<OrderItemEntity> val) {
            items = val;
            return this;
        }

        public OrderEntity build() {
            return new OrderEntity(this);
        }
    }
}

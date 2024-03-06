package br.com.moraesit.order.service.infra.persistence.order.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@IdClass(OrderItemEntityId.class)
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    private Long id;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;


    public OrderItemEntity() {
    }

    public OrderItemEntity(Long id, OrderEntity order, UUID productId, BigDecimal price, Integer quantity, BigDecimal subTotal) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    private OrderItemEntity(Builder builder) {
        id = builder.id;
        order = builder.order;
        productId = builder.productId;
        price = builder.price;
        quantity = builder.quantity;
        subTotal = builder.subTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public UUID getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItemEntity that = (OrderItemEntity) o;

        if (!id.equals(that.id)) return false;
        return order.equals(that.order);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + order.hashCode();
        return result;
    }

    public static final class Builder {
        private Long id;
        private OrderEntity order;
        private UUID productId;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subTotal;

        private Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder order(OrderEntity val) {
            order = val;
            return this;
        }

        public Builder productId(UUID val) {
            productId = val;
            return this;
        }

        public Builder price(BigDecimal val) {
            price = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder subTotal(BigDecimal val) {
            subTotal = val;
            return this;
        }

        public OrderItemEntity build() {
            return new OrderItemEntity(this);
        }
    }
}

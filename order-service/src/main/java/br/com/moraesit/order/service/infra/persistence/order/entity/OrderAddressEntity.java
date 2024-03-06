package br.com.moraesit.order.service.infra.persistence.order.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "order_address")
public class OrderAddressEntity {

    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    private String street;
    private String postalCode;
    private String city;


    public OrderAddressEntity() {
    }

    public OrderAddressEntity(UUID id, OrderEntity order, String street, String postalCode, String city) {
        this.id = id;
        this.order = order;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    private OrderAddressEntity(Builder builder) {
        id = builder.id;
        order = builder.order;
        street = builder.street;
        postalCode = builder.postalCode;
        city = builder.city;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderAddressEntity that = (OrderAddressEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final class Builder {
        private UUID id;
        private OrderEntity order;
        private String street;
        private String postalCode;
        private String city;

        private Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder order(OrderEntity val) {
            order = val;
            return this;
        }

        public Builder street(String val) {
            street = val;
            return this;
        }

        public Builder postalCode(String val) {
            postalCode = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public OrderAddressEntity build() {
            return new OrderAddressEntity(this);
        }
    }
}

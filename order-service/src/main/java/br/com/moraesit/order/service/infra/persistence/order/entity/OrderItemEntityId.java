package br.com.moraesit.order.service.infra.persistence.order.entity;

import java.io.Serializable;

public class OrderItemEntityId implements Serializable {

    private Long id;
    private OrderEntity order;

    public OrderItemEntityId() {
    }

    public OrderItemEntityId(Long id, OrderEntity order) {
        this.id = id;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItemEntityId that = (OrderItemEntityId) o;

        if (!id.equals(that.id)) return false;
        return order.equals(that.order);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + order.hashCode();
        return result;
    }
}

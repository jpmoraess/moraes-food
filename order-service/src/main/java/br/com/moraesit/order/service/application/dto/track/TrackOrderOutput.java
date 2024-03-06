package br.com.moraesit.order.service.application.dto.track;

import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderOutput {
    @NotNull
    private UUID orderTrackingId;
    @NotNull
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private TrackOrderOutput(Builder builder) {
        orderTrackingId = builder.orderTrackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }


    public UUID getOrderTrackingId() {
        return orderTrackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private @NotNull UUID orderTrackingId;
        private @NotNull OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderTrackingId(@NotNull UUID val) {
            orderTrackingId = val;
            return this;
        }

        public Builder orderStatus(@NotNull OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public TrackOrderOutput build() {
            return new TrackOrderOutput(this);
        }
    }
}

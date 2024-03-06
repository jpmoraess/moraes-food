package br.com.moraesit.order.service.application.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderInput {

    @NotNull
    private UUID orderTrackingId;

    private TrackOrderInput(Builder builder) {
        orderTrackingId = builder.orderTrackingId;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private @NotNull UUID orderTrackingId;

        private Builder() {
        }

        public Builder orderTrackingId(@NotNull UUID val) {
            orderTrackingId = val;
            return this;
        }

        public TrackOrderInput build() {
            return new TrackOrderInput(this);
        }
    }

    public UUID getOrderTrackingId() {
        return orderTrackingId;
    }
}

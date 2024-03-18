package br.com.moraesit.order.service.application.dto.track;

import br.com.moraesit.commons.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackOrderOutput {

    @NotNull
    private UUID orderTrackingId;

    @NotNull
    private OrderStatus orderStatus;

    private List<String> failureMessages;
}

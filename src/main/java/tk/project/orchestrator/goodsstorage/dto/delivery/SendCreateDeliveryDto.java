package tk.project.orchestrator.goodsstorage.dto.delivery;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SendCreateDeliveryDto {

    private final UUID orderId;

    private final String deliveryAddress;
}

package tk.project.orchestrator.goodsstorage.dto.delivery;

import lombok.Data;

import java.util.UUID;

@Data
public class SendCreateDeliveryDto {

    private UUID orderId;

    private String deliveryAddress;
}

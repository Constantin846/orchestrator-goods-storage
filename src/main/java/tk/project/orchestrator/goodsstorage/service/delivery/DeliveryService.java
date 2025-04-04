package tk.project.orchestrator.goodsstorage.service.delivery;

import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.SendCreateDeliveryDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDateDto sendRequestCreateDelivery(SendCreateDeliveryDto deliveryDto);

    void sendRequestDeleteDelivery(UUID orderId);
}

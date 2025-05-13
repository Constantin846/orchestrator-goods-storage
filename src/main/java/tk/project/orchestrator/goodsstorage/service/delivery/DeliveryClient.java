package tk.project.orchestrator.goodsstorage.service.delivery;

import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.SendCreateDeliveryDto;

import java.util.UUID;

public interface DeliveryClient {
    DeliveryDateDto sendRequestCreateDelivery(final SendCreateDeliveryDto deliveryDto);

    void sendRequestDeleteDelivery(final UUID orderId);
}

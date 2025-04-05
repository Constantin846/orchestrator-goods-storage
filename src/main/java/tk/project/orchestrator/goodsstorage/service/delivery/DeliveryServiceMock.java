package tk.project.orchestrator.goodsstorage.service.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.SendCreateDeliveryDto;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnExpression("'${app.delivery-service.type}'.equals('mock')")
public class DeliveryServiceMock implements DeliveryService {

    @Override
    public DeliveryDateDto sendRequestCreateDelivery(SendCreateDeliveryDto deliveryDto) {
        log.info("DeliveryServiceMock send request to create delivery");
        return new DeliveryDateDto(LocalDate.now().plusDays(3));
    }

    @Override
    public void sendRequestDeleteDelivery(UUID orderId) {
        log.info("DeliveryServiceMock send request to delete delivery");
    }
}

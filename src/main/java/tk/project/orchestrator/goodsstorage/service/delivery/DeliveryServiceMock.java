package tk.project.orchestrator.goodsstorage.service.delivery;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.SendCreateDeliveryDto;

import java.util.UUID;

@Slf4j
@Service
@ConditionalOnExpression("'${app.delivery-service.type}'.equals('mock')")
public class DeliveryServiceMock implements DeliveryService {

    @Override
    public DeliveryDateDto sendRequestCreateDelivery(SendCreateDeliveryDto deliveryDto) {
        return new DeliveryDateDto(LocalDate.now().plusDays(3));
    }

    @Override
    public void sendRequestDeleteDelivery(UUID orderId) {
    }
}

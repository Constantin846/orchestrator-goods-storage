package tk.project.orchestrator.goodsstorage.delegate.delivery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.SendCreateDeliveryDto;
import tk.project.orchestrator.goodsstorage.service.delivery.DeliveryService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationDelivery implements JavaDelegate {
    private final DeliveryService deliveryService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Create delivery, business key: {}", delegateExecution.getBusinessKey());

        try {
            SendCreateDeliveryDto deliveryDto = new SendCreateDeliveryDto();
            deliveryDto.setOrderId((UUID) delegateExecution.getVariable("id"));
            deliveryDto.setDeliveryAddress((String) delegateExecution.getVariable("deliveryAddress"));

            DeliveryDateDto deliveryDateDto = deliveryService.sendRequestCreateDelivery(deliveryDto);

            delegateExecution.setVariable("deliveryDate", deliveryDateDto.getDate());

        } catch (Exception e) {
            throw new BpmnError("delegateCancelableError", e);
        }
    }
}

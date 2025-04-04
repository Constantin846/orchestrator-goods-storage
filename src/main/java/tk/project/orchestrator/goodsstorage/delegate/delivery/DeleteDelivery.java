package tk.project.orchestrator.goodsstorage.delegate.delivery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.service.delivery.DeliveryService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteDelivery implements JavaDelegate {
    private final DeliveryService deliveryService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Delete delivery, business key: {}", delegateExecution.getBusinessKey());

        deliveryService.sendRequestDeleteDelivery(
                (UUID) delegateExecution.getVariable("id")
        );
    }
}

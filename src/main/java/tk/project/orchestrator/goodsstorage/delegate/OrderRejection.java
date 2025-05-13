package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.enums.OrderStatus;
import tk.project.orchestrator.goodsstorage.service.product.ProductClient;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRejection implements JavaDelegate {
    private final ProductClient productClient;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        log.info("Reject order, business key: {}", delegateExecution.getBusinessKey());

        final SetOrderStatusRequest orderStatusDto = SetOrderStatusRequest.builder()
                .orderId((UUID) delegateExecution.getVariable("id"))
                .status(OrderStatus.REJECTED)
                .build();

        productClient.sendRequestSetOrderStatus(orderStatusDto);
    }
}

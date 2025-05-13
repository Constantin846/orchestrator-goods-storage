package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.enums.OrderStatus;
import tk.project.orchestrator.goodsstorage.service.product.ProductClient;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConfirming implements JavaDelegate {
    private final ProductClient productClient;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        log.info("Change order status, business key: {}", delegateExecution.getBusinessKey());

        SetOrderStatusRequest orderStatusDto;

        if ((Boolean) delegateExecution.getVariable("errorFlag")) {
            orderStatusDto = buildSetOrderStatusRequest(
                    (UUID) delegateExecution.getVariable("id"),
                    OrderStatus.REJECTED,
                    null
            );
        } else {
            orderStatusDto = buildSetOrderStatusRequest(
                    (UUID) delegateExecution.getVariable("id"),
                    OrderStatus.CONFIRMED,
                    (LocalDate) delegateExecution.getVariable("deliveryDate")
            );
        }
        productClient.sendRequestSetOrderStatus(orderStatusDto);
    }

    private SetOrderStatusRequest buildSetOrderStatusRequest(
            final UUID orderId, final OrderStatus status, @Nullable final LocalDate deliveryDate
    ) {
        return SetOrderStatusRequest.builder()
                .orderId(orderId)
                .status(status)
                .deliveryDate(deliveryDate)
                .build();
    }
}

package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.product.OrderStatus;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusDto;
import tk.project.orchestrator.goodsstorage.service.product.ProductService;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConfirming implements JavaDelegate {
    private final ProductService productService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Change order status, business key: {}", delegateExecution.getBusinessKey());

        SetOrderStatusDto orderStatusDto = new SetOrderStatusDto();
        orderStatusDto.setOrderId((UUID) delegateExecution.getVariable("id"));

        if ((Boolean) delegateExecution.getVariable("errorFlag")) {
           orderStatusDto.setStatus(OrderStatus.REJECTED);
        } else {
            orderStatusDto.setStatus(OrderStatus.CONFIRMED);
            orderStatusDto.setDeliveryDate((LocalDate) delegateExecution.getVariable("deliveryDate"));
        }
        productService.sendRequestSetOrderStatus(orderStatusDto);
    }
}

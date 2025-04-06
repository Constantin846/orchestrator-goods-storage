package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.product.OrderStatus;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.service.product.ProductService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRejection implements JavaDelegate {
    private final ProductService productService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Reject order, business key: {}", delegateExecution.getBusinessKey());

        SetOrderStatusRequest orderStatusDto = new SetOrderStatusRequest();
        orderStatusDto.setOrderId((UUID) delegateExecution.getVariable("id"));
        orderStatusDto.setStatus(OrderStatus.REJECTED);
        productService.sendRequestSetOrderStatus(orderStatusDto);
    }
}

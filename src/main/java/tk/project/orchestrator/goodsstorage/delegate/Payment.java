package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.payment.PayOrderDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;
import tk.project.orchestrator.goodsstorage.service.payment.PaymentClient;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Payment implements JavaDelegate {
    private final PaymentClient paymentClient;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        log.info("Pay for order, business key: {}", delegateExecution.getBusinessKey());

        if ((Boolean) delegateExecution.getVariable("errorFlag")) {
            log.info("Pay for order was cancelled, errorFlag is true");
            return;
        }

        try {
            final PayOrderDto payOrderDto = PayOrderDto.builder()
                    .orderId((UUID) delegateExecution.getVariable("id"))
                    .price((BigDecimal) delegateExecution.getVariable("price"))
                    .accountNumber((String) delegateExecution.getVariable("accountNumber"))
                    .build();

            final PaymentResultDto paymentResult = paymentClient.sendRequestPayOrder(payOrderDto);

            if (!paymentResult.getIsSuccessful()) {
                throw new BpmnError("delegateCancelableError");
            }

        } catch (Exception e) {
            throw new BpmnError("delegateCancelableError", e);
        }
    }
}

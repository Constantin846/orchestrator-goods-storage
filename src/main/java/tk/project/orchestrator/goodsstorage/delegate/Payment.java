package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.payment.PayOrderDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;
import tk.project.orchestrator.goodsstorage.service.payment.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Payment implements JavaDelegate {
    private final PaymentService paymentService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Pay for order, business key: {}", delegateExecution.getBusinessKey());

        if ((Boolean) delegateExecution.getVariable("errorFlag")) {
            log.info("Pay for order was cancelled, errorFlag is true");
            return;
        }

        try {
            PayOrderDto payOrderDto = new PayOrderDto();
            payOrderDto.setOrderId((UUID) delegateExecution.getVariable("id"));
            payOrderDto.setPrice((BigDecimal) delegateExecution.getVariable("price"));
            payOrderDto.setAccountNumber((String) delegateExecution.getVariable("accountNumber"));

            PaymentResultDto paymentResult = paymentService.sendRequestPayOrder(payOrderDto);

            if (!paymentResult.getIsSuccessful()) {
                throw new BpmnError("delegateCancelableError");
            }

        } catch (Exception e) {
            throw new BpmnError("delegateCancelableError", e);
        }
    }
}

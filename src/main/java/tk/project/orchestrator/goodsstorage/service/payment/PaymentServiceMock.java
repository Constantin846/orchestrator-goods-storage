package tk.project.orchestrator.goodsstorage.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.payment.PayOrderDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;

@Slf4j
@Service
@ConditionalOnExpression("'${app.payment-service.type}'.equals('mock')")
public class PaymentServiceMock implements PaymentService {

    @Override
    public PaymentResultDto sendRequestPayOrder(PayOrderDto payOrderDto) {
        log.info("PaymentServiceMock send request pay order");
        return new PaymentResultDto(true);
    }
}

package tk.project.orchestrator.goodsstorage.service.payment;

import tk.project.orchestrator.goodsstorage.dto.payment.PayOrderDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;

public interface PaymentClient {
    PaymentResultDto sendRequestPayOrder(final PayOrderDto payOrderDto);
}

package tk.project.orchestrator.goodsstorage.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tk.project.orchestrator.goodsstorage.dto.payment.PayOrderDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;

@Slf4j
@Service
@ConditionalOnMissingBean(PaymentServiceMock.class)
public class PaymentServiceImpl implements PaymentService {
    private final String uriPayForOrder;
    private final WebClient webClient;

    public PaymentServiceImpl(
            @Value("${payment-service.method.pay-for-order}")
            String uriPayForOrder,
            @Autowired
            @Qualifier("paymentWebClient")
            WebClient webClient
    ) {
        this.uriPayForOrder = uriPayForOrder;
        this.webClient = webClient;
    }

    @Override
    public PaymentResultDto sendRequestPayOrder(PayOrderDto payOrderDto) {
        return webClient.post()
                .uri(uriPayForOrder)
                .bodyValue(payOrderDto)
                .retrieve()
                .bodyToMono(PaymentResultDto.class)
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to pay for order";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }
}

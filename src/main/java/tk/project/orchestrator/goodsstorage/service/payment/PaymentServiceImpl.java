package tk.project.orchestrator.goodsstorage.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import tk.project.orchestrator.goodsstorage.dto.payment.PayOrderDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;

import java.time.Duration;

@Slf4j
@Service
@ConditionalOnMissingBean(PaymentServiceMock.class)
public class PaymentServiceImpl implements PaymentService {
    private static final int RETRY_COUNT = 2;
    private final String uriPayForOrder;
    private final long timeout;
    private final WebClient webClient;

    public PaymentServiceImpl(
            @Value("${payment-service.method.pay-for-order}")
            String uriPayForOrder,
            @Value("${payment-service.timeout}")
            long timeout,
            @Autowired
            @Qualifier("paymentWebClient")
            WebClient webClient
    ) {
        this.uriPayForOrder = uriPayForOrder;
        this.timeout = timeout;
        this.webClient = webClient;
    }

    @Override
    public PaymentResultDto sendRequestPayOrder(PayOrderDto payOrderDto) {
        return webClient.patch()
                .uri(uriPayForOrder)
                .bodyValue(payOrderDto)
                .retrieve()
                .bodyToMono(PaymentResultDto.class)
                .retryWhen(Retry.fixedDelay(RETRY_COUNT, Duration.ofMillis(timeout)))
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to pay for order";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }
}

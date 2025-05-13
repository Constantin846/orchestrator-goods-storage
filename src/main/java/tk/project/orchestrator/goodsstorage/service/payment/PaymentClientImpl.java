package tk.project.orchestrator.goodsstorage.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@ConditionalOnMissingBean(PaymentClientMock.class)
public class PaymentClientImpl implements PaymentClient {
    private final String uriPayForOrder;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public PaymentClientImpl(
            @Value("${payment-service.method.pay-for-order}")
            final String uriPayForOrder,
            @Autowired
            @Qualifier("paymentWebClient")
            final WebClient webClient
    ) {
        this.uriPayForOrder = uriPayForOrder;
        this.webClient = webClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public PaymentResultDto sendRequestPayOrder(final PayOrderDto payOrderDto) {
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

package tk.project.orchestrator.goodsstorage.service.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.SendCreateDeliveryDto;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnMissingBean(DeliveryClientMock.class)
public class DeliveryClientImpl implements DeliveryClient {
    private static final int RETRY_COUNT = 2;
    private final String uriCreateDelivery;
    private final String uriDeleteDelivery;
    private final long timeout;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public DeliveryClientImpl(
            @Value("${delivery-service.method.create}")
            final String uriCreateDelivery,
            @Value("${delivery-service.method.delete}")
            final String uriDeleteDelivery,
            @Value("${delivery-service.timeout}")
            final long timeout,
            @Autowired
            @Qualifier("deliveryWebClient")
            final WebClient webClient
    ) {
        this.uriCreateDelivery = uriCreateDelivery;
        this.uriDeleteDelivery = uriDeleteDelivery;
        this.timeout = timeout;
        this.webClient = webClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public DeliveryDateDto sendRequestCreateDelivery(final SendCreateDeliveryDto deliveryDto) {
        return webClient.post()
                .uri(uriCreateDelivery)
                .bodyValue(deliveryDto)
                .retrieve()
                .bodyToMono(DeliveryDateDto.class)
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to create delivery";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }

    @Override
    public void sendRequestDeleteDelivery(final UUID orderId) {
        webClient.delete()
                .uri(uriDeleteDelivery, Map.of("orderId", orderId))
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(RETRY_COUNT, Duration.ofMillis(timeout)))
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to delete delivery";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }
}

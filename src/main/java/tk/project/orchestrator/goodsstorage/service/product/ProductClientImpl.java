package tk.project.orchestrator.goodsstorage.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusResponse;

import java.time.Duration;

@Slf4j
@Service
@ConditionalOnMissingBean(ProductClientMock.class)
public class ProductClientImpl implements ProductClient {
    private static final int RETRY_COUNT = 2;
    private final String orchestratorId;
    private final String uriSetOrderStatus;
    private final long timeout;
    private final WebClient webClient;

    public ProductClientImpl(
            @Value("${app.orchestrator-id}")
            final String orchestratorId,
            @Value("${product-service.method.set-status}")
            final String uriSetOrderStatus,
            @Value("${product-service.timeout}")
            final long timeout,
            @Autowired
            @Qualifier("productWebClient")
            final WebClient webClient
    ) {
        this.orchestratorId = orchestratorId;
        this.uriSetOrderStatus = uriSetOrderStatus;
        this.timeout = timeout;
        this.webClient = webClient;
    }

    @Override
    public SetOrderStatusResponse sendRequestSetOrderStatus(final SetOrderStatusRequest orderStatusDto) {
        return webClient.patch()
                .uri(uriSetOrderStatus)
                .header("X_Orchestrator_ID", orchestratorId)
                .bodyValue(orderStatusDto)
                .retrieve()
                .bodyToMono(SetOrderStatusResponse.class)
                .retryWhen(Retry.fixedDelay(RETRY_COUNT, Duration.ofMillis(timeout)))
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to set order status";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }
}

package tk.project.orchestrator.goodsstorage.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import tk.project.orchestrator.goodsstorage.dto.CustomerNotificationDto;

import java.time.Duration;

@Slf4j
@Service
@ConditionalOnMissingBean(NotificationServiceMock.class)
public class NotificationServiceImpl implements NotificationService {
    private static final int RETRY_COUNT = 2;
    private final String uriCreateCustomerNotification;
    private final long timeout;
    private final WebClient webClient;

    public NotificationServiceImpl(
            @Value("${notification-service.method.send-message}")
            String uriCreateCustomerNotification,
            @Value("${notification-service.timeout}")
            long timeout,
            @Autowired
            @Qualifier("notificationWebClient")
            WebClient webClient
    ) {
        this.uriCreateCustomerNotification = uriCreateCustomerNotification;
        this.timeout = timeout;
        this.webClient = webClient;
    }

    @Override
    public void sendCustomerNotification(CustomerNotificationDto notificationDto) {
        webClient.post()
                .uri(uriCreateCustomerNotification)
                .bodyValue(notificationDto)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(RETRY_COUNT, Duration.ofMillis(timeout)))
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to create customer notification";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }
}

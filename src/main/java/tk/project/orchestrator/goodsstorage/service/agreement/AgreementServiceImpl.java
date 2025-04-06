package tk.project.orchestrator.goodsstorage.service.agreement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;

import java.time.Duration;

@Slf4j
@Service
@ConditionalOnMissingBean(AgreementServiceMock.class)
public class AgreementServiceImpl implements AgreementService {
    private static final int RETRY_COUNT = 2;
    private final String uriCreateAgreement;
    private final String uriDeleteAgreement;
    private final long timeout;
    private final WebClient webClient;

    public AgreementServiceImpl(
            @Value("${agreement-service.method.create-agreement}")
            String uriCreateAgreement,
            @Value("${agreement-service.method.delete-agreement}")
            String uriDeleteAgreement,
            @Value("${agreement-service.timeout}")
            long timeout,
            @Autowired
            @Qualifier("agreementWebClient")
            WebClient webClient
    ) {
        this.uriCreateAgreement = uriCreateAgreement;
        this.uriDeleteAgreement = uriDeleteAgreement + "/";
        this.timeout = timeout;
        this.webClient = webClient;
    }

    @Override
    public String sendRequestCreateAgreement(AgreementDataDto agreementData) {
        return webClient.post()
                .uri(uriCreateAgreement)
                .bodyValue(agreementData)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to create agreement";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }

    @Override
    public void sendRequestDeleteAgreement(String agreementId) {
        webClient.delete()
                .uri(uriDeleteAgreement + agreementId)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(RETRY_COUNT, Duration.ofMillis(timeout)))
                .doOnError(error -> {
                    String message = "Something went wrong while executing request to delete agreement";
                    log.warn(message);
                    throw new RuntimeException(message, error);
                })
                .block();
    }
}

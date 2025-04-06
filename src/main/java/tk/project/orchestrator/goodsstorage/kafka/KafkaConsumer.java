package tk.project.orchestrator.goodsstorage.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.controller.ConfirmOrderController;
import tk.project.orchestrator.goodsstorage.dto.ComplianceResultDto;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled", matchIfMissing = false)
public class KafkaConsumer {

    private final ConfirmOrderController confirmOrderController;

    @KafkaListener(topics = "brokerage-compliance-info", containerFactory = "kafkaListenerContainerFactoryString")
    public void listen(String message) throws JsonProcessingException {
        log.info("Receive message: {}", message);
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final ComplianceResultDto complianceResult = objectMapper.readValue(message, ComplianceResultDto.class);
            log.info("ComplianceResultDto: {}", complianceResult);

            confirmOrderController.messageFromCompliance(complianceResult);

        } catch (JsonProcessingException e) {
            String msg = String.format("Couldn't parse message: %s; exception: %s", message, e);
            log.warn(msg);
            throw new RuntimeException(msg);
        }
    }
}

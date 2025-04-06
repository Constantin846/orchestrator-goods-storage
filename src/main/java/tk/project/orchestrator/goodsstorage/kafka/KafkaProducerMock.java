package tk.project.orchestrator.goodsstorage.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnMissingBean(KafkaProducerImpl.class)
public class KafkaProducerMock implements KafkaProducer {

    @Override
    public String getClaimTopic() {
        return "mock";
    }

    @Override
    public void sendMessage(final String topic, final String key, final Object event) {
        log.info("KafkaProducerMock send message");
    }
}
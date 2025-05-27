package tk.project.orchestrator.goodsstorage;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public interface KafkaTestContainer {

    @Container
    KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:6.1.1")
    );

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.kafka.bootstrapAddress", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("app.kafka.enabled", KafkaTestContainer::setKafkaEnabledTrue);
    }

    static String setKafkaEnabledTrue() {
        return Boolean.TRUE.toString();
    }
}

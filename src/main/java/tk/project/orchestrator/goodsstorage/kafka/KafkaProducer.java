package tk.project.orchestrator.goodsstorage.kafka;

public interface KafkaProducer {

    String getOrchestratorTopic();

    String getComplianceInfoTopic();

    void sendMessage(final String topic, final String key, final Object event);
}

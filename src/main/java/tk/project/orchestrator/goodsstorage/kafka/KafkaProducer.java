package tk.project.orchestrator.goodsstorage.kafka;

public interface KafkaProducer {

    String getClaimTopic();

    void sendMessage(final String topic, final String key, final Object event);
}

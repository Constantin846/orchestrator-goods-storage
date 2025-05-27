package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.ComplianceDto;
import tk.project.orchestrator.goodsstorage.kafka.KafkaProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class Compliance implements JavaDelegate {
    private final KafkaProducer kafkaProducer;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        log.info("Send request to compliance, business key: {}", delegateExecution.getBusinessKey());

        final ComplianceDto complianceDto = ComplianceDto.builder()
                .login((String) delegateExecution.getVariable("login"))
                .inn((String) delegateExecution.getVariable("inn"))
                .businessKey(delegateExecution.getBusinessKey())
                .build();

        kafkaProducer.sendMessage(kafkaProducer.getOrchestratorTopic(), complianceDto.getBusinessKey(), complianceDto);
    }
}

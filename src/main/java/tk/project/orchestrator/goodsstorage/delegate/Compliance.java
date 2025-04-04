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
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Send to compliance, business key: {}", delegateExecution.getBusinessKey());

        ComplianceDto complianceDto = new ComplianceDto();
        complianceDto.setLogin((String) delegateExecution.getVariable("login"));
        complianceDto.setInn((String) delegateExecution.getVariable("inn"));
        complianceDto.setBusinessKey(delegateExecution.getBusinessKey());

        kafkaProducer.sendMessage(KafkaProducer.CLAIM_TOPIC, complianceDto.getBusinessKey(), complianceDto);
    }
}

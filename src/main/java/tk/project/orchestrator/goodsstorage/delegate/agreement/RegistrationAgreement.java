package tk.project.orchestrator.goodsstorage.delegate.agreement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;
import tk.project.orchestrator.goodsstorage.service.agreement.AgreementClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationAgreement implements JavaDelegate {
    private final AgreementClient agreementClient;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        log.info("Create agreement, business key: {}", delegateExecution.getBusinessKey());

        final AgreementDataDto agreementData = AgreementDataDto.builder()
                .inn((String) delegateExecution.getVariable("inn"))
                .accountNumber((String) delegateExecution.getVariable("accountNumber"))
                .build();

        final String agreementId = agreementClient.sendRequestCreateAgreement(agreementData);
        delegateExecution.setVariable("agreementId", agreementId);
    }
}

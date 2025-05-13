package tk.project.orchestrator.goodsstorage.delegate.agreement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.service.agreement.AgreementClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteAgreement implements JavaDelegate {
    private final AgreementClient agreementClient;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        log.info("Delete agreement, business key: {}", delegateExecution.getBusinessKey());

        agreementClient.sendRequestDeleteAgreement(
                (String) delegateExecution.getVariable("agreementId")
        );
    }
}

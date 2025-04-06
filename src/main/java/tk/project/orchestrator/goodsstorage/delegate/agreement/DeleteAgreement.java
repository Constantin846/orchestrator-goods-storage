package tk.project.orchestrator.goodsstorage.delegate.agreement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.service.agreement.AgreementService;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteAgreement implements JavaDelegate {
    private final AgreementService agreementService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Delete agreement, business key: {}", delegateExecution.getBusinessKey());

        agreementService.sendRequestDeleteAgreement(
                (String) delegateExecution.getVariable("agreementId")
        );
    }
}

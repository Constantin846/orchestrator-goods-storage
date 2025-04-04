package tk.project.orchestrator.goodsstorage.delegate.agreement;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.service.agreement.AgreementService;

@Component
@RequiredArgsConstructor
public class DeleteAgreement implements JavaDelegate {
    private final AgreementService agreementService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        agreementService.sendRequestDeleteAgreement(
                (String) delegateExecution.getVariable("agreementId")
        );
    }
}

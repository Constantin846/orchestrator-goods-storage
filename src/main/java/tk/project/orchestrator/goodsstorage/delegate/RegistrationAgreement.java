package tk.project.orchestrator.goodsstorage.delegate;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;
import tk.project.orchestrator.goodsstorage.service.agreement.AgreementService;

@Component
@RequiredArgsConstructor
public class RegistrationAgreement implements JavaDelegate {
    private final AgreementService agreementService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        AgreementDataDto agreementData = new AgreementDataDto();
        agreementData.setInn((String) delegateExecution.getVariable("inn"));
        agreementData.setAccountNumber((String) delegateExecution.getVariable("accountNumber"));

        String agreementId = agreementService.sendRequestCreateAgreement(agreementData);
        delegateExecution.setVariable("agreementId", agreementId);
    }
}

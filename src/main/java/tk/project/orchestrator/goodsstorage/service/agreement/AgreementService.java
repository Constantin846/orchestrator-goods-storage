package tk.project.orchestrator.goodsstorage.service.agreement;

import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;

public interface AgreementService {
    String sendRequestCreateAgreement(AgreementDataDto agreementData);
}

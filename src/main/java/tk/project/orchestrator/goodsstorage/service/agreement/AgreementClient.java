package tk.project.orchestrator.goodsstorage.service.agreement;

import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;

public interface AgreementClient {
    String sendRequestCreateAgreement(final AgreementDataDto agreementData);

    void sendRequestDeleteAgreement(final String agreementId);
}

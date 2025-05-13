package tk.project.orchestrator.goodsstorage.service.agreement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;

import java.util.Objects;

@Slf4j
@Service
@ConditionalOnExpression("'${app.agreement-client.type}'.equals('mock')")
public class AgreementClientMock implements AgreementClient {
    @Override
    public String sendRequestCreateAgreement(final AgreementDataDto agreementData) {
        log.info("AgreementServiceMock send request to create agreement");
        final int number = Objects.hash(agreementData.getInn(), agreementData.getAccountNumber());
        return Integer.toString(number);
    }

    @Override
    public void sendRequestDeleteAgreement(final String agreementId) {
        log.info("AgreementServiceMock send request to delete agreement");
    }
}

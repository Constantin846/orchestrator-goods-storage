package tk.project.orchestrator.goodsstorage.service.agreement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;

import java.util.Objects;

@Slf4j
@Service
@ConditionalOnExpression("'${app.agreement-service.type}'.equals('mock')")
public class AgreementServiceMock implements AgreementService {
    @Override
    public String sendRequestCreateAgreement(AgreementDataDto agreementData) {
        int number = Objects.hash(agreementData.getInn(), agreementData.getAccountNumber());
        return Integer.toString(number);
    }
}

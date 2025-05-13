package tk.project.orchestrator.goodsstorage.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AgreementDataDto {

    private final String inn;

    private final String accountNumber;
}

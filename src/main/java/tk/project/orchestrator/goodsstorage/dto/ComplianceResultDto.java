package tk.project.orchestrator.goodsstorage.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tk.project.orchestrator.goodsstorage.enums.ComplianceStatus;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ComplianceResultDto {

    private final UUID businessKey;

    private final ComplianceStatus complianceStatus;
}

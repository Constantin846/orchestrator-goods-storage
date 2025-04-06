package tk.project.orchestrator.goodsstorage.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ComplianceResultDto {

    private UUID businessKey;

    private ComplianceStatus complianceStatus;
}
